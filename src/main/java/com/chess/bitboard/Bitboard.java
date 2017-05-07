package com.chess.bitboard;

/**
 * Bitboard class represented by a 64-bit long.
 *
 * @author Kevin Crosby
 */
public class Bitboard {
  private static final int N = 8;
  private static final int N2 = N * N;

  private static final long MAX_VALUE = -1L;
  private static final long MIN_VALUE = 0L;

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
    this.board = board.board;
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

  public void not() {
    board = MAX_VALUE - board;
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

  private void check(final int i) {
    assert i >= 0 && i < N2 : String.format("Index %s out of bounds!", Long.toUnsignedString(i));
  }

  private void check(final int r, final int f) {
    assert r >= 0 && r < N : String.format("Rank %s out of bounds!", Long.toUnsignedString(r));
    assert f >= 0 && f < N : String.format("File %s out of bounds!", Long.toUnsignedString(f));
  }

  private int index(final int r, final int f) {
    check(r, f);
    return r * N + f;
  }

  private int rank(final int i) {
    check(i);
    return i / N;
  }

  private int file(final int i) {
    check(i);
    return i % N;
  }

  private int mask(final int r, final int f) {
    return mask(index(r, f));
  }

  private int mask(final int i) {
    check(i);
    return 1 << i;
  }

  /**
   * Test rank and file of bitboard.
   *
   * @param r Rank of bitboard.
   * @param f File of bitboard.
   * @return True if bitboard at rank and file is set, or false otherwise.
   */
  public boolean test(final int r, final int f) {
    return test(index(r, f));
  }

  private boolean test(final int i) {
    return (board & mask(i)) != 0;
  }

  /**
   * Toggle rank and file of bitboard.
   *
   * @param r Rank of bitboard.
   * @param f File of bitboard.
   */
  public void toggle(final int r, final int f) {
    toggle(index(r, f));
  }

  private void toggle(final int i) {
    board ^= mask(i);
  }

  /**
   * Clear rank and file of bitboard.
   *
   * @param r Rank of bitboard.
   * @param f File of bitboard.
   */
  public void clear(final int r, final int f) {
    clear(index(r, f));
  }

  private void clear(final int i) {
    board &= (MAX_VALUE - mask(i));
  }

  /**
   * Set rank and file of bitboard.
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
}
