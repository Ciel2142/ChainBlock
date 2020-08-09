package blockchain;

import java.io.File;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.*;

public class BlockChain {

    private static final long serialVersionUID = 2L;
    private final List<Block> chain;
    private final boolean SAVE;
    private final File PATH;
    private int currentID;
    private int zeros;

    public BlockChain() {
        this(null);
    }

    public BlockChain(File file) {
        this.SAVE = file != null;
        this.PATH = file;
        this.chain = new ArrayList<>();
        this.addBlock();
        if (this.SAVE) {
            this.saveChain();
        }
    }

    public String getZeroString() {
        return this.zeros > 0 ? "0".repeat(this.zeros) : "";
    }

    public void addBlock() {
        this.currentID++;
        ExecutorService executor = Executors.newFixedThreadPool(
                Runtime.getRuntime().availableProcessors());
        Instant timeToCreate = Instant.now();

        Future<Block> blockFuture = executor.submit(
                new Miner(getPreviousHash(), getZeroString(), this.currentID, timeToCreate));
        try {
            this.chain.add(blockFuture.get());
            executor.shutdownNow();
            executor.awaitTermination(300L, TimeUnit.MILLISECONDS);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        if (!validateChain()) {
            this.chain.remove(this.chain.size() - 1);
            this.addBlock();
            return;
        }
        long time = this.chain.get(chain.size() - 1).getTIME_TO_CREATE_HASH();
        if (time < 20) {
            this.zeros++;
        } else if (time > 40) {
            this.zeros--;
        }

        if (this.SAVE) {
            this.saveChain();
        }
    }

    private void saveChain() {
        SerializeBlockChain.Serialize(this, this.PATH);
    }

    private boolean validateChain() {
        Iterator<Block> chainIterator = this.chain.iterator();
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
        return this.chain.size() > 0 ?
                this.chain.get(chain.size() - 1).getCurrentHash() : "0";
    }

    public void printChain() {
        for (Block block : this.chain) {
            System.out.println(block);
            System.out.println();
        }
    }

}
