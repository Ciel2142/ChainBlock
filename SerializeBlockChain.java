package blockchain;

import java.io.*;

public class SerializeBlockChain {

    public static void Serialize(Object object, File path) {
        try {
            FileOutputStream ous = new FileOutputStream(path);
            BufferedOutputStream bos = new BufferedOutputStream(ous);
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(object);
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static BlockChain deserialize(File path) {
        try {
            FileInputStream fis = new FileInputStream(path);
            BufferedInputStream bis = new BufferedInputStream(fis);
            ObjectInputStream ois = new ObjectInputStream(bis);
            Object object = ois.readObject();
            ois.close();
            return (BlockChain) object;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
