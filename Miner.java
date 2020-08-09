package blockchain;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.Callable;

public class Miner implements Callable<Block> {

    private final String prevHash;
    private final String zeroString;
    private final int id;
    private final Instant time;

    public Miner(String prevHash, String zeroString, int id, Instant startedMaking) {
        this.prevHash = prevHash;
        this.zeroString = zeroString;
        this.id = id;
        this.time = startedMaking;
    }

    @Override
    public Block call() {
        long magicNumber = HashUtility.generateMagicNumber(this.zeroString);
        String hash = HashUtility.applySha256(String.valueOf(magicNumber));
        long minerId = Thread.currentThread().getId();
        long created = Duration.between(this.time, Instant.now()).toSeconds();
        String message = created < 14 ?
                String.format("N was increased to %d",
                this.zeroString.length() + 1) :
                (created < 40 ? "N stays the same" : "N was decreased by 1");
        return new Block(prevHash, hash, message, id, minerId, magicNumber, created);
    }
}
