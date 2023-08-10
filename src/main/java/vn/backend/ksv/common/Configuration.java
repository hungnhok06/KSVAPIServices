package vn.backend.ksv.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import io.vertx.core.Context;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import vn.backend.ksv.common.exception.CommonException;

import java.io.*;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Creator: Nguyen Quang Hung
 * Date: 8/8/23
 * Time: 4:08 PM
 */
public class Configuration {
    private static final LogAdapter LOGGER = LogAdapter.newInstance(Configuration.class);

    private static JsonObject rawConfig;


    /**
     * Read config file with list urls of config file
     *
     * @param context vertx context in abstract vertical extended class
     * @return POJO config from JsonObject variable
     */
    public static <T> T loadConfig(Context context, Class<T> clazz) {
        try {
            LOGGER.info("loading properties");
            Json.prettyMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            Json.mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            Json.mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            rawConfig = new JsonObject();
            List urls = context
                    .config()
                    .getJsonArray("url")
                    .getList();
            for (Object url : urls) {
                loadFile(String.valueOf(url));
            }
            return Json.decodeValue(rawConfig.encodePrettily(), clazz);
        } catch (Exception e) {
            LOGGER.error("Error on load config file cause by {}", e.getMessage());
            throw new CommonException.ConfigException("Error on load config file");
        }

    }

    /**
     * Read config file with and parse into model T
     *
     * @param config json config file
     * @return POJO config from JsonObject variable
     */
    public static <T> T loadProperties(String config, Class<T> clazz) {
        try {
            LOGGER.info("loading properties");
            T object = new Gson().fromJson(config, clazz);
            LOGGER.trace("config file: {}", new GsonBuilder().setPrettyPrinting().create().toJson((object)));
            checkValidationAllProperty(object, "config file");
            LOGGER.info("properties loaded successfully");
            return object;
        } catch (JsonSyntaxException e) {
            LOGGER.error("Invalid config cause {}", e.getMessage());
            LOGGER.info("properties loaded failed");
            throw new CommonException.InvalidConfig(e.getMessage());
        } catch (CommonException.InvalidConfig e) {
            LOGGER.error(e.getMessage());
            LOGGER.info("properties loaded failed");
            throw new CommonException.InvalidConfig(e.getMessage());
        } catch (Exception e) {
            LOGGER.error("Unknown error cause by {}", e.getMessage());
            LOGGER.info("properties loaded failed");
            throw new CommonException.InvalidConfig(e.getMessage());
        }
    }

    /**
     * load single config file into static JsonObject variable
     *
     * @param url of config file
     */
    private static void loadFile(String url) {
        String content = FileHelper.readFile(url);
        rawConfig.mergeIn(new JsonObject(content));
    }

    public static void checkValidation(Object obj) {
        try {
            if (obj == null) {
                throw new CommonException.InvalidConfig("Object is null");
            }
            for (Field f : obj.getClass().getDeclaredFields()) {
                f.setAccessible(true);
                if (f.get(obj) == null) {
                    throw new CommonException.InvalidConfig("config " + f.getName() + " is null");
                }
            }
        } catch (IllegalAccessException e) {
            LOGGER.error("Error with accessible object cause by {} ", e.getMessage());
            LOGGER.info("properties loaded failed");
            throw new CommonException.InvalidConfig(e.getMessage());
        }
    }

    private static void checkValidationAllProperty(Object obj, String configName) {
        try {
            if (obj == null) {
                throw new CommonException.InvalidConfig("Object is null");
            }
            for (Field f : obj.getClass().getDeclaredFields()) {
                f.setAccessible(true);
                if (f.get(obj) == null) {
                    throw new CommonException.InvalidConfig("config " + f.getName() + " in " + configName + " is null");
                }
//                LOGGER.debug("type: {}",f.get(obj).getClass().getSimpleName().toLowerCase());
                switch (f.get(obj).getClass().getSimpleName().toLowerCase()) {
                    case "string":
                        break;
                    case "integer":
                        if ((Integer) f.get(obj) < 0)
                            throw new CommonException.InvalidConfig("config " + f.getName() + " in " + configName + " is is negative");
                        break;
                    case "boolean":
                        break;
                    case "long":
                        if ((Long) f.get(obj) < 0)
                            throw new CommonException.InvalidConfig("config " + f.getName() + " in " + configName + " is is negative");
                        break;
                    case "double":
                        if ((Double) f.get(obj) < 0)
                            throw new CommonException.InvalidConfig("config " + f.getName() + " in " + configName + " is is negative");
                        break;
                    case "bigdecimal":
                        if (((BigDecimal) f.get(obj)).compareTo(new BigDecimal(0)) < 0)
                            throw new CommonException.InvalidConfig("config " + f.getName() + " in " + configName + " is is negative");
                        break;
                    case "biginteger":
                        if (((BigInteger) f.get(obj)).compareTo(BigInteger.valueOf(0)) < 0)
                            throw new CommonException.InvalidConfig("config " + f.getName() + " in " + configName + " is is negative");
                        break;
                    default:
                        checkValidationAllProperty(f.get(obj), f.getName());
                        break;
                }
            }
        } catch (CommonException.InvalidConfig e) {
            LOGGER.error("Invalid config: {} - {}", configName, e.getMessage());
            throw new CommonException.InvalidConfig(e.getMessage());
        } catch (IllegalAccessException e) {
            LOGGER.error("Error with accessible object cause by {} ", e.getMessage());
            LOGGER.info("properties loaded failed");
            throw new CommonException.InvalidConfig(e.getMessage());
        }
    }

    public static String decodeStringFile(String fileName) throws Exception {
        FileReader fr = new FileReader(fileName);
        BufferedReader br = new BufferedReader(fr);
        StringBuilder result = new StringBuilder();
        String sCurrentLine;

        while ((sCurrentLine = br.readLine()) != null) {
            result.append(sCurrentLine);
        }
        return result.toString();
    }

    public static byte[] decodeByteFile(String fileName) throws IOException {
        File f = new File(fileName);
        FileInputStream fis = new FileInputStream(f);
        DataInputStream dis = new DataInputStream(fis);
        byte[] keyBytes = new byte[fis.available()];
        dis.readFully(keyBytes);
        dis.close();
        return keyBytes;
    }

    public static <T> T loadPropertiesByFile(String configFile, Class<T> clazz) {
        try {
            LOGGER.info("loading properties by file");
            String contents = new String(Files.readAllBytes(Paths.get(configFile)));
            return loadProperties(contents, clazz);
        } catch (JsonSyntaxException e) {
            LOGGER.error("Invalid config cause {}", e.getMessage());
            LOGGER.info("properties loaded failed");
            throw new CommonException.InvalidConfig(e.getMessage());
        } catch (CommonException.InvalidConfig e) {
            LOGGER.error(e.getMessage());
            LOGGER.info("properties loaded failed");
            throw new CommonException.InvalidConfig(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("Unknown error cause by {}", e.getMessage());
            LOGGER.info("properties loaded failed");
            throw new CommonException.InvalidConfig(e.getMessage());
        }
    }
}
