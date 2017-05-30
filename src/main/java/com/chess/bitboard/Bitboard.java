package com.chess.bitboard;

import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Bitboard class represented by a 64-bit long.
 *
 * @author Kevin Crosby
 */
public class Bitboard {
  private static final int N = 8;
  private static final int N2 = N * N;

  private static final ImmutableBiMap<Integer, Character> RANKS =
      IntStream.range(0, N).boxed().collect(ImmutableBiMap.toImmutableBiMap(i -> i, "12345678"::charAt));
  private static final ImmutableBiMap<Integer, Character> FILES =
      IntStream.range(0, N).boxed().collect(ImmutableBiMap.toImmutableBiMap(i -> i, "abcdefgh"::charAt));

  private static final long MAX_VALUE = -1L;
  private static final long MIN_VALUE = 0L;

  private static final long MASK = 0xffffffffffffffffL;
  private static final long RANK_MASK = 0xffL;
  private static final long FILE_MASK = 0x101010101010101L;
  private static final long DIAG_MASK = 0x102040810204080L;
  private static final long ANTI_MASK = 0x8040201008040201L;

  private static final long A_MASK = 0xfefefefefefefefeL;
  private static final long H_MASK = 0x7f7f7f7f7f7f7f7fL;

  private long board;

  /**
   * Create blank bitboard.
   */
  public Bitboard() {
    this.board = MIN_VALUE;
  }

  /**
   * Create bitboard based on unsigned long value.
   *
   * @param board Unsigned long value.
   */
  public Bitboard(final long board) {
    this.board = board;
  }

  /**
   * Copy bitboard.
   *
   * @param board Bitboard to copy from.
   */
  public Bitboard(final Bitboard board) {
    this(board.board);
  }

  /**
   * Create bitboard based on unsigned long value.
   *
   * @param board Unsigned long value.
   * @return Created bitboard;
   */
  public static Bitboard of(final long board) {
    return new Bitboard(board);
  }

  /**
   * Copy bitboard.
   *
   * @param board Bitboard to copy from.
   * @return Copied bitboard;
   */
  public static Bitboard of(final Bitboard board) {
    return new Bitboard(board);
  }

  /**
   * Clear bitboard with all zeros.
   */
  public void clear() {
    board = MIN_VALUE;
  }

  /**
   * Set bitboard with all ones.
   */
  public void set() {
    board = MAX_VALUE;
  }

  /**
   * Boolean NOT of bitboard.
   *
   * @return Boolean NOT of bitboard.
   */
  public Bitboard not() {
    return new Bitboard(~board);
  }

  /**
   * Boolean AND of bitboards.
   *
   * @param board Bitboard to AND.
   * @return Boolean AND of bitboards.
   */
  public Bitboard and(final Bitboard board) {
    return Bitboard.of(this.board & board.board);
  }

  /**
   * Boolean OR of bitboards.
   *
   * @param board Bitboard to OR.
   * @return Boolean OR of bitboards.
   */
  public Bitboard or(final Bitboard board) {
    return Bitboard.of(this.board | board.board);
  }

  /**
   * Boolean XOR of bitboards.
   *
   * @param board Bitboard to XOR.
   * @return Boolean XOR of bitboards.
   */
  public Bitboard xor(final Bitboard board) {
    return Bitboard.of(this.board ^ board.board);
  }

  /**
   * Shift left of bitboard.
   *
   * @param shift Amount to shift.
   * @return Shifted bitboard.
   */
  public Bitboard shiftLeft(final int shift) {
    return Bitboard.of(this.board << shift); // TODO check for files a,b g,h
  }

  /**
   * Shift right of bitboard.
   *
   * @param shift Amount to shift.
   * @return Shifted bitboard.
   */
  public Bitboard shiftRight(final int shift) {
    return Bitboard.of(this.board >>> shift); // TODO check for files a,b g,h
  }

  private static void check(final int i) {
    assert i >= 0 && i < N2 : String.format("Index %d out of bounds!", i);
  }

  private static void check(final int r, final int f) {
    checkRank(r);
    checkFile(f);
  }

  private static void checkRank(final int r) {
    assert r >= 0 && r < N : String.format("Rank %d out of bounds!", r);
  }

  private static void checkFile(final int f) {
    assert f >= 0 && f < N : String.format("File %d out of bounds!", f);
  }

  private static void checkDiag(final int d) {
    assert d >= 1 - N && d <= N - 1 : String.format("Diagonal %d out of bounds!", d);
  }

  private static int index(final int r, final int f) {
    check(r, f);
    return r * N + f;
  }

  private static int rank(final int i) {
    check(i);
    return i / N;
  }

  private static int file(final int i) {
    check(i);
    return i % N;
  }

  private static long mask(final int r, final int f) {
    return mask(index(r, f));
  }

  private static long mask(final int i) {
    check(i);
    return 1L << i;
  }

  private static long rankMask(final int r) {
    checkRank(r);
    return RANK_MASK << (r * N);
  }

  private static long fileMask(final int f) {
    checkFile(f);
    return FILE_MASK << f;
  }

  private static long diagMask(final int diag) {
    checkDiag(diag);
    long mask = DIAG_MASK;
    if (diag > 0) {
      for (int d = 0; d < diag; ++d) {
        mask <<= 1;
        mask &= A_MASK;
      }
    } else if (diag < 0) {
      for (int d = 0; d > diag; --d) {
        mask >>>= 1;
        mask &= H_MASK;
      }
    }
    return mask;
  }

  private static long antiMask(final int anti) {
    checkDiag(anti);
    long mask = ANTI_MASK;
    if (anti > 0) {
      for (int a = 0; a < anti; ++a) {
        mask >>>= 1;
        mask &= H_MASK;
      }
    } else if (anti < 0) {
      for (int a = 0; a > anti; --a) {
        mask <<= 1;
        mask &= A_MASK;
      }
    }
    return mask;
  }

  /**
   * Create rank bitboard.
   *
   * @param r Row of bitboard to set (0 <= r <= N - 1).
   * @return Bitboard with specified rank set.
   */
  public static Bitboard rankBoard(final int r) {
    return Bitboard.of(rankMask(r));
  }

  /**
   * Create file bitboard.
   *
   * @param f File of bitboard to set (0 <= f <= N - 1).
   * @return Bitboard with specified file set.
   */
  public static Bitboard fileBoard(final int f) {
    return Bitboard.of(fileMask(f));
  }

  /**
   * Create diagonal bitboard.
   *
   * @param d Diagonal of bitboard to set (1 - N <= d <= N - 1).
   *          A negative index is below the diagonal.
   * @return Bitboard with specified diagonal set.
   */
  public static Bitboard diagBoard(final int d) {
    return Bitboard.of(diagMask(d));
  }

  /**
   * Create anti-diagonal bitboard.
   *
   * @param a Anti-diagonal of bitboard to set (1 - N <= a <= N - 1).
   *          A negative index is below the anti-diagonal.
   * @return Bitboard with specified anti-diagonal set.
   */
  public static Bitboard antiBoard(final int a) {
    return Bitboard.of(antiMask(a));
  }

  /**
   * Get value at rank and file of bitboard.
   *
   * @param r Rank of bitboard.
   * @param f File of bitboard.
   * @return True if bitboard at rank and file is set, or false otherwise.
   */
  public boolean get(final int r, final int f) {
    return get(index(r, f));
  }

  private boolean get(final int i) {
    return (board & mask(i)) != 0L;
  }

  /**
   * Flip value at rank and file of bitboard.
   *
   * @param r Rank of bitboard.
   * @param f File of bitboard.
   */
  public void flip(final int r, final int f) {
    flip(index(r, f));
  }

  private void flip(final int i) {
    board ^= mask(i);
  }

  /**
   * Clear value at rank and file of bitboard.
   *
   * @param r Rank of bitboard.
   * @param f File of bitboard.
   */
  public void clear(final int r, final int f) {
    clear(index(r, f));
  }

  private void clear(final int i) {
    board &= ~mask(i);
  }

  /**
   * Set value at rank and file of bitboard.
   *
   * @param r Rank of bitboard.
   * @param f File of bitboard.
   */
  public void set(final int r, final int f) {
    set(index(r, f));
  }

  private void set(final int i) {
    board |= mask(i);
  }

  // TODO nextBitInRow, nextBitInFile, nextBitInDiagonal, nextBitInAntiDiagonal
  // TODO nextSetBit, nextSetBitInRow, nextSetBitInFile, nextSetBitInDiagonal, nextSetBitInAntiDiagonal
  // TODO nextClearBit, nextClearBitInRow, nextClearBitInFile, nextClearBitInDiagonal, nextClearBitInAntiDiagonal
  // TODO cardinality

  @Override
  public String toString() {
    List<String> ranks = Lists.newArrayList();
    long m, mask;
    int r, f;
    for (r = N - 1, m = mask(r, 0); r >= 0; --r, m >>>= N) {
      List<String> file = Lists.newArrayList(RANKS.get(r).toString());
      for (f = 0, mask = m; f < N; ++f, mask <<= 1) {
        String square = (board & mask) != 0L ? "*" : ".";
        file.add(square);
      }
      ranks.add(String.join(" ", file));
    }
    String footer = FILES.values().stream()
        .map(Object::toString)
        .collect(Collectors.collectingAndThen(Collectors.joining(" "), x -> "  " + x));
    ranks.add(footer);

    return String.join("\n", ranks);
  }

  public static void main(String[] args) {
    for (int r = 0; r < N; ++r) {
      System.out.println(rankBoard(r));
    }
    for (int f = 0; f < N; ++f) {
      System.out.println(fileBoard(f));
    }
    for (int d = 1 - N; d < N; ++d) {
      System.out.println(diagBoard(d));
    }
    for (int a = 1 - N; a < N; ++a) {
      System.out.println(antiBoard(a));
    }
  }
}
