package io.github.tedpaulsen.chess.lib;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import lombok.Value;
import org.apache.commons.lang3.StringUtils;

@Value
public class BitBoard {

    long value;

    public boolean empty() {
        return value == 0;
    }

    public BitBoard shiftLeft(int leftShift) {
        return new BitBoard(value << leftShift);
    }

    public BitBoard shiftRight(int rightShift) {
        return new BitBoard(value >>> rightShift);
    }

    public BitBoard notAFile() {
        return new BitBoard(value & ~Masks.A_FILE);
    }

    public BitBoard notBFile() {
        return new BitBoard(value & ~Masks.B_FILE);
    }

    public BitBoard notGFile() {
        return new BitBoard(value & ~Masks.G_FILE);
    }

    public BitBoard notHFile() {
        return new BitBoard(value & ~Masks.H_FILE);
    }

    public BitBoard rank2() {
        return new BitBoard(value & Masks.RANK_2);
    }

    public BitBoard rank7() {
        return new BitBoard(value & Masks.RANK_7);
    }

    public BitBoard notRank1() {
        return new BitBoard(value & ~Masks.RANK_1);
    }

    public BitBoard notRank8() {
        return new BitBoard(value & ~Masks.RANK_8);
    }

    public BitBoard mask(BitBoard b) {
        return new BitBoard(value & b.value);
    }

    public BitBoard mask(long m) {
        return new BitBoard(value & m);
    }

    /**
     * Expands this BitBoard to a list of singleton bitboards.
     * e.g. 001      000   001
     *      100 -> [ 100 , 000 ]
     *      000      000   000
     */
    public Collection<BitBoard> toSingletons() {
        if (isSingleton()) {
            return Set.of(new BitBoard(value));
        }

        long v = value;
        int pow = 0;
        Collection<BitBoard> c = new HashSet<>();
        while (v > 0) {
            long rem = v % 2;
            if (rem != 0) {
                c.add(new BitBoard(rem << pow));
            }
            v = v >> 1;
            pow++;
        }
        return c;
    }

    /**
     * Returns true if and only if the bitboard contains a single piece
     */
    public boolean isSingleton() {
        boolean singleton = false;
        for (char c : Long.toBinaryString(value).toCharArray()) {
            if (c == '1') {
                if (singleton) {
                    return false;
                }
                singleton = true;
            }
        }
        return singleton;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        String bStr = StringUtils.leftPad(Long.toBinaryString(value), 64, '0');
        char rank = '8';
        for (int i = 0; i < 64; i += 8) {
            sb.append(rank--);
            sb.append(" ");
            sb.append(new StringBuilder().append(bStr, i, i + 8).reverse());
            sb.append("\n");
        }
        sb.append("  abcdefgh");
        return sb.toString().replace('0', '.');
    }
}
