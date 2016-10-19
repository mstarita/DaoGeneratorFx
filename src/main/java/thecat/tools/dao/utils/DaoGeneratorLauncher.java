package thecat.tools.dao.utils;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Paths;
import java.util.List;

public class DaoGeneratorLauncher {

    private static final String DAO_GENERATOR_JAR = "DaoGenerator-all-0.4.5.jar";

    public static int exec(List<String> classPathList, List<String> params) throws Exception {
        URL[] urls = new URL[classPathList.size() + 1];

        for (int i=0; i<classPathList.size(); i++) {
            urls[i] = new File(classPathList.get(i)).toURI().toURL();
        }

        urls[classPathList.size()] = new File(Paths.get("./" + DAO_GENERATOR_JAR).toAbsolutePath().normalize().toString()).toURI().toURL();

        URLClassLoader classLoader = new URLClassLoader(urls);
        Class daoGenerator = Class.forName("thecat.tools.dao.DaoGenerator", true, classLoader);
        Method mainMethod = daoGenerator.getMethod("main", String[].class);

        String[] paramArr = new String[params.size()];
        params.toArray(paramArr);

        mainMethod.invoke(daoGenerator.newInstance(), new Object[] { paramArr });

        return -1;
    }
    
}
