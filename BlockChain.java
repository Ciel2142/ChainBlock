package blockchain;

import java.io.File;
import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.*;

public class BlockChain implements Serializable {

    private static final long serialVersionUID = 3L;
    private final List<Block> CHAIN;
    private final boolean SAVE;
    private final File PATH;
    private int currentID;
    private int zeros;
    private final List<Message> MESSAGES;

    public BlockChain() {
        this(null);
    }

    public BlockChain(File file) {
        this.SAVE = file != null;
        this.PATH = file;
        this.CHAIN = new ArrayList<>();
        this.MESSAGES = new ArrayList<>();
        this.addBlock();
        if (this.SAVE) {
            this.saveChain();
        }
    }

    private String getZeroString() {
        return this.zeros > 0 ? "0".repeat(this.zeros) : "";
    }


    public void addBlock() {
        this.currentID++;
        ExecutorService minerExecutor = Executors.newFixedThreadPool(
                Runtime.getRuntime().availableProcessors());
        ExecutorService messageStream = Executors.newSingleThreadExecutor();

        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Future<Block> blockFuture = minerExecutor.submit(new Miner(getPreviousHash(),
                getZeroString(), this.currentID, Instant.now(), this.consolidateMessages()));

        this.MESSAGES.clear();

        Messages messagesRun = new Messages(this.MESSAGES);
        messageStream.submit(messagesRun);

        try {
            this.CHAIN.add(blockFuture.get());
            minerExecutor.shutdownNow();
            minerExecutor.awaitTermination(100L, TimeUnit.MILLISECONDS);
            messagesRun.stop();
            messageStream.awaitTermination(100L, TimeUnit.MILLISECONDS);
            if (!validateChain()) {
                this.currentID--;
                this.CHAIN.remove(this.CHAIN.size() - 1);
                this.addBlock();
                return;
            }
            this.changeZeroValue(this.CHAIN.get(CHAIN.size() - 1).
                    getTIME_TO_CREATE_HASH());
            if (this.SAVE) {
                this.saveChain();
            }
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private String consolidateMessages() {
        if (this.CHAIN.size() == 0) {
            return "Block data: no messages\n";
        }
        StringBuilder blockData = new StringBuilder("Block data:\n");
        this.MESSAGES.forEach(message -> blockData.append(message).append("\n"));
        return blockData.toString();
    }

    private void changeZeroValue(long time) {
        if (time < 20) {
            this.zeros++;
        } else if (time > 40) {
            this.zeros--;
        }
    }

    private void saveChain() {
        SerializeBlockChain.Serialize(this, this.PATH);
    }

    private boolean validateChain() {
        Iterator<Block> chainIterator = this.CHAIN.iterator();
        String hash = chainIterator.next().getCurrentHash();
        Block currBlock;

        while (chainIterator.hasNext()) {
            currBlock = chainIterator.next();
            if (validateHash(currBlock.getPreviousHash(), hash)) {
                hash = currBlock.getCurrentHash();
            } else {
                return false;
            }
        }
        return true;
    }

    private boolean validateHash(String curr, String prev) {
        return curr.equals(prev);
    }

    private String getPreviousHash() {
        return this.CHAIN.size() > 0 ?
                this.CHAIN.get(CHAIN.size() - 1).getCurrentHash() : "0";
    }

    public void printChain() {
        for (Block block : this.CHAIN) {
            System.out.println(block);
            System.out.println();
        }
    }

}
