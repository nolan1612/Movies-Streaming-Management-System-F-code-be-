package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RecentWatchStack implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final int MAX_SIZE = 5;

    private List<String> stack;

    public RecentWatchStack() {
        this.stack = new ArrayList<>();
    }

    public void push(String movieId) {
        stack.remove(movieId);
        if (stack.size() >= MAX_SIZE) {
            stack.remove(0);
        }
        stack.add(movieId);
    }

    public String pop() {
        if (stack.isEmpty()) return null;
        return stack.remove(stack.size() - 1);
    }

    public String peek() {
        if (stack.isEmpty()) return null;
        return stack.get(stack.size() - 1);
    }

    public List<String> getRecentMovies() {
        List<String> reversed = new ArrayList<>();
        for (int i = stack.size() - 1; i >= 0; i--) {
            reversed.add(stack.get(i));
        }
        return reversed;
    }

    public int size() { return stack.size(); }
    public void clear() { stack.clear(); }
}