package am.iunetworks.mf2351.pkcs11;

import java.io.File;
import java.lang.reflect.Field;
import java.security.KeyStore;
import java.security.KeyStoreSpi;
import java.security.Provider;
import java.security.Security;
import java.security.cert.X509Certificate;
import java.util.*;

public class PKCS11Utils {
	
    public static boolean registerProvider(final String configPath) {

        final File cfgFile = new File(configPath);
        final String absolutePath = cfgFile.getAbsolutePath();
        if (cfgFile.isFile()) {
            System.out.println("Trying to register PKCS#11 provider at " + absolutePath);
            try {
                Security.addProvider((Provider) Class.forName("sun.security.pkcs11.SunPKCS11")
                        .getConstructor(String.class).newInstance(absolutePath));
                System.out.println("PKCS#11 provider registered successfully!");
                return true;
            }
            catch (Exception e) {
                System.err.println("Unable to register security provider.");
                System.err.println(e.getMessage());
                e.printStackTrace();
//                e.printStackTrace();
            }
        }
        else {
            System.err.println("The PKCS#11 provider is not registered. Configuration file doesn't exist: "
                    + absolutePath);
        }
        return false;
    }

    public static SortedSet<String> getKeyStores() {
        final Set<String> tmpKeyStores = Security.getAlgorithms("KeyStore");
        return new TreeSet<String>(tmpKeyStores);
    }

    public static String[] getCertAliases(KeyStore tmpKs) {
        if (tmpKs == null)
            return null;
        final List<String> tmpResult = new ArrayList<String>();
        try {
            final Enumeration<String> tmpAliases = tmpKs.aliases();
            while (tmpAliases.hasMoreElements()) {
                final String tmpAlias = tmpAliases.nextElement();
//                if (tmpKs.isCertificateEntry(tmpAlias)) {
                    tmpResult.add(tmpAlias);
//                }
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
//            e.printStackTrace();
            return null;
        }
        return tmpResult.toArray(new String[tmpResult.size()]);
    }

    // Official workaround from Sun for bug #6672015
    // http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6672015
    @SuppressWarnings("unchecked")
    public static void fixAliases(final KeyStore keyStore) {
        Field field;
        KeyStoreSpi keyStoreVeritable;
        final Set<String> tmpAliases = new HashSet<String>();
        try {
          field = keyStore.getClass().getDeclaredField("keyStoreSpi");
          field.setAccessible(true);
          keyStoreVeritable = (KeyStoreSpi) field.get(keyStore);

          if ("sun.security.mscapi.KeyStore$MY".equals(keyStoreVeritable.getClass().getName())) {
            Collection<Object> entries;
            String alias, hashCode;
            X509Certificate[] certificates;

            field = keyStoreVeritable.getClass().getEnclosingClass().getDeclaredField("entries");
            field.setAccessible(true);
            entries = (Collection<Object>) field.get(keyStoreVeritable);

            for (Object entry : entries) {
              field = entry.getClass().getDeclaredField("certChain");
              field.setAccessible(true);
              certificates = (X509Certificate[]) field.get(entry);

              hashCode = Integer.toString(certificates[0].hashCode());

              field = entry.getClass().getDeclaredField("alias");
              field.setAccessible(true);
              alias = (String) field.get(entry);
              String tmpAlias = alias;
              int i = 0;
              while (tmpAliases.contains(tmpAlias)) {
                i++;
                tmpAlias = alias + "-" + i;
              }
              tmpAliases.add(tmpAlias);
              if (!alias.equals(hashCode)) {
                field.set(entry, tmpAlias);
              }
            }
          }
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
          // nothing to do here
        }
      }    
}
