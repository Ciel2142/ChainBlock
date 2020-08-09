package blockchain;

import java.io.Serializable;
import java.util.Date;

class Block implements Serializable {

    private static final long serialVersionUID = 3L;
    private final String PREVIOUS_HASH;
    private final String CURRENT_HASH;
    private final long ID;
    private final long TIME_STAMP;
    private final long TIME_TO_CREATE_HASH;
    private final long MAGIC_NUMBER;
    private final long MINER_ID;
    private final String MESSAGE;


    public Block(String PREVIOUS_HASH, String hash, String message,
                 int ID, long minerId, long magicNumber, long timeToCreate) {
        this.TIME_TO_CREATE_HASH = timeToCreate;
        this.MAGIC_NUMBER = magicNumber;
        this.CURRENT_HASH = hash;
        this.MESSAGE = message;
        this.MINER_ID = minerId;
        this.PREVIOUS_HASH = PREVIOUS_HASH;
        this.ID = ID;
        this.TIME_STAMP = new Date().getTime();
    }

    public long getTIME_TO_CREATE_HASH() {
        return this.TIME_TO_CREATE_HASH;
    }

    public String toString() {
        return String.format("Block:%n" +
                        "Created by minter # %d%n" +
                        "Id: %d%n" +
                        "Timestamp: %d%n" +
                        "Magic number: %d%n" +
                        "Hash of the previous block:%n" +
                        "%s%n" +
                        "Hash of the block:%n" +
                        "%s%n" +
                        "Block was generating for %d seconds%n" +
                        "%s",
                this.MINER_ID,
                this.ID,
                this.TIME_STAMP,
                this.MAGIC_NUMBER,
                this.PREVIOUS_HASH,
                this.CURRENT_HASH,
                this.TIME_TO_CREATE_HASH,
                this.MESSAGE);
    }

    public String getCurrentHash() {
        return this.CURRENT_HASH;
    }

    public String getPreviousHash() {
        return this.PREVIOUS_HASH;
    }
}
