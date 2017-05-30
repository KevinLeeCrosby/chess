package com.chess.puzzle;

import com.chess.bitboard.Bitboard;
import com.google.common.collect.Lists;
import com.google.common.collect.Queues;

import java.util.Collection;
import java.util.List;
import java.util.Queue;

/**
 * 8 Queens Puzzle
 *
 * @author Kevin Crosby
 */
public class EightQueens {
  private static final int N = 8;

  private class Node {
    private final Bitboard board;
    private final int rank;

    private Node() {
      this.board = new Bitboard();
      this.rank = -1;
    }

    private Node(final Bitboard parent, final int rank, final int file) {
      assert rank >= 0 && rank < N : String.format("Rank %d out of bounds!", rank);
      assert file >= 0 && file < N : String.format("File %d out of bounds!", file);
      this.board = Bitboard.of(parent);
      this.rank = rank;
      for (int f = 0; f < N; ++f) {
        board.set(rank, f); // label rank
      }
      for (int r = rank; r < N; ++r) {
        board.set(r, file); // label rest of column
      }
      for (int r = rank, f = file; r < N && f < N; ++r, ++f) {
        board.set(r, f); // label rest of diagonal
      }
      for (int r = rank, f = file; r < N && f >= 0; ++r, --f) {
        board.set(r, f); // label rest of anti-diagonal
      }
      board.clear(rank, file);
    }

    public Bitboard getBoard() {
      return board;
    }

    public int getRank() {
      return rank;
    }

    public Collection<Node> children() {
      List<Node> nodes = Lists.newArrayList();
      int r = rank + 1;
      if (r < N) {
        for (int f = 0; f < N; ++f) {
          if (!board.get(r, f)) {
            nodes.add(new Node(board, r, f));
          }
        }
      }
      return nodes;
    }
  }

  public void solve() {
    Node node = new Node(); // empty bitboard

    Queue<Node> open = Queues.newArrayDeque();
    open.add(node);

    int count = 0;
    while (!open.isEmpty()) {
      node = open.remove();
      if (node.getRank() == N - 1) {
        System.out.println(++count);
        System.out.println(node.getBoard().not());
      } else {
        open.addAll(node.children());
      }
    }
  }

  public static void main(String[] args) {
    EightQueens problem = new EightQueens();

    problem.solve();
  }
}
