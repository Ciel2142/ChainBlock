package blockchain;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        File file = new File(args.length > 0 ? args[0] : "");

        BlockChain chain;
        if (file.exists()) {
            chain = SerializeBlockChain.deserialize(file);
            if (chain == null) {
                chain = new BlockChain(file);
            }
        } else {
            chain = new BlockChain();
        }

        for (int i = 0; i < 4; i++) {
            chain.addBlock();
        }

        chain.printChain();
    }
}
