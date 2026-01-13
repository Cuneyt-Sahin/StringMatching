import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Arrays;
class Naive extends Solution {
    static {
        SUBCLASSES.add(Naive.class);
        System.out.println("Naive registered");
    }

    public Naive() {
    }

    @Override
    public String Solve(String text, String pattern) {
        List<Integer> indices = new ArrayList<>();
        int n = text.length();
        int m = pattern.length();

        for (int i = 0; i <= n - m; i++) {
            int j;
            for (j = 0; j < m; j++) {
                if (text.charAt(i + j) != pattern.charAt(j)) {
                    break;
                }
            }
            if (j == m) {
                indices.add(i);
            }
        }

        return indicesToString(indices);
    }
}

class KMP extends Solution {
    static {
        SUBCLASSES.add(KMP.class);
        System.out.println("KMP registered");
    }

    public KMP() {
    }

    @Override
    public String Solve(String text, String pattern) {
        List<Integer> indices = new ArrayList<>();
        int n = text.length();
        int m = pattern.length();

        // Handle empty pattern - matches at every position
        if (m == 0) {
            for (int i = 0; i <= n; i++) {
                indices.add(i);
            }
            return indicesToString(indices);
        }

        // Compute LPS (Longest Proper Prefix which is also Suffix) array
        int[] lps = computeLPS(pattern);

        int i = 0; // index for text
        int j = 0; // index for pattern

        while (i < n) {
            if (text.charAt(i) == pattern.charAt(j)) {
                i++;
                j++;
            }

            if (j == m) {
                indices.add(i - j);
                j = lps[j - 1];
            } else if (i < n && text.charAt(i) != pattern.charAt(j)) {
                if (j != 0) {
                    j = lps[j - 1];
                } else {
                    i++;
                }
            }
        }

        return indicesToString(indices);
    }

    private int[] computeLPS(String pattern) {
        int m = pattern.length();
        int[] lps = new int[m];
        int len = 0;
        int i = 1;

        lps[0] = 0;

        while (i < m) {
            if (pattern.charAt(i) == pattern.charAt(len)) {
                len++;
                lps[i] = len;
                i++;
            } else {
                if (len != 0) {
                    len = lps[len - 1];
                } else {
                    lps[i] = 0;
                    i++;
                }
            }
        }

        return lps;
    }
}

class RabinKarp extends Solution {
    static {
        SUBCLASSES.add(RabinKarp.class);
        System.out.println("RabinKarp registered.");
    }

    public RabinKarp() {
    }

    private static final int PRIME = 101; // A prime number for hashing

    @Override
    public String Solve(String text, String pattern) {
        List<Integer> indices = new ArrayList<>();
        int n = text.length();
        int m = pattern.length();

        // Handle empty pattern - matches at every position
        if (m == 0) {
            for (int i = 0; i <= n; i++) {
                indices.add(i);
            }
            return indicesToString(indices);
        }

        if (m > n) {
            return "";
        }

        int d = 256; // Number of characters in the input alphabet
        long patternHash = 0;
        long textHash = 0;
        long h = 1;

        // Calculate h = d^(m-1) % PRIME
        for (int i = 0; i < m - 1; i++) {
            h = (h * d) % PRIME;
        }

        // Calculate hash value for pattern and first window of text
        for (int i = 0; i < m; i++) {
            patternHash = (d * patternHash + pattern.charAt(i)) % PRIME;
            textHash = (d * textHash + text.charAt(i)) % PRIME;
        }

        // Slide the pattern over text one by one
        for (int i = 0; i <= n - m; i++) {
            // Check if hash values match
            if (patternHash == textHash) {
                // Check characters one by one
                boolean match = true;
                for (int j = 0; j < m; j++) {
                    if (text.charAt(i + j) != pattern.charAt(j)) {
                        match = false;
                        break;
                    }
                }
                if (match) {
                    indices.add(i);
                }
            }

            // Calculate hash value for next window
            if (i < n - m) {
                textHash = (d * (textHash - text.charAt(i) * h) + text.charAt(i + m)) % PRIME;

                // Convert negative hash to positive
                if (textHash < 0) {
                    textHash = textHash + PRIME;
                }
            }
        }

        return indicesToString(indices);
    }
}

/**
 * TODO: Implement Boyer-Moore algorithm
 * This is a homework assignment for students
 */
class BoyerMoore extends Solution {
    static {
        SUBCLASSES.add(BoyerMoore.class);
        System.out.println("BoyerMoore registered");
    }

    public BoyerMoore() {
    }

    @Override
    public String Solve(String text, String pattern) {
        List<Integer> indices = new ArrayList<>();
        int n = text.length();
        int m = pattern.length();

        //We handle edge cases here: empty pattern or pattern > text
        if (m == 0) {
            for (int i = 0; i <= n; i++) indices.add(i);
            return indicesToString(indices);
        }
        if (m > n) return "";

        //Here we determine table size ASCII or Unicode
        int tableSize = 256;
        for (int i = 0; i < m; i++) {
            if (pattern.charAt(i) >= 256) {
                tableSize = 65536;
                break;
            }
        }


        int[] badChar = new int[tableSize];
        Arrays.fill(badChar, -1);


        for (int i = 0; i < m; i++) {

            badChar[pattern.charAt(i)] = i;
        }

        int s = 0;
        //Here we loop until pattern goes beyond text length
        while (s <= (n - m)) {
            int j = m - 1;

            while (j >= 0 && pattern.charAt(j) == text.charAt(s + j)) {
                j--;
            }

            if (j < 0) {
                indices.add(s);
                if (s + m < n) {
                    char nextChar = text.charAt(s + m);

                    int shift = (nextChar < tableSize) ? badChar[nextChar] : -1;
                    s += m - shift;
                } else {
                    s += 1;
                }
            } else {
                char textChar = text.charAt(s + j);
                int shift = (textChar < tableSize) ? badChar[textChar] : -1;
                s += Math.max(1, j - shift);
            }
        }
        return indicesToString(indices);
    }

}

/**
 * TODO: Implement your own creative string matching algorithm
 * This is a homework assignment for students
 * Be creative! Try to make it efficient for specific cases
 */
class GoCrazy extends Solution {
    static {
        SUBCLASSES.add(GoCrazy.class);
        System.out.println("GoCrazy (Sunday Algorithm) registered");
    }

    public GoCrazy() {
    }

    @Override
    public String Solve(String text, String pattern) {
        List<Integer> indices = new ArrayList<>();
        int n = text.length();
        int m = pattern.length();

        //Here we handle edge cases
        if (m == 0) {
            for (int i = 0; i <= n; i++) indices.add(i);
            return indicesToString(indices);
        }
        if (m > n) return "";

        int[] asciiShifts = new int[256];
        Arrays.fill(asciiShifts, m + 1);

        //Here we use a map for Unicode characters to save memory
        Map<Character, Integer> unicodeShifts = null;

        //Here we calculate shift values
        for (int i = 0; i < m; i++) {
            char c = pattern.charAt(i);
            int shift = m - i;

            if (c < 256) {
                asciiShifts[c] = shift;
            } else {
                if (unicodeShifts == null) unicodeShifts = new HashMap<>();
                unicodeShifts.put(c, shift);
            }
        }

        int s = 0;
        while (s <= n - m) {
            //Here we are checking for match
            int j = 0;
            while (j < m && text.charAt(s + j) == pattern.charAt(j)) {
                j++;
            }

            //If full match found record index
            if (j == m) {
                indices.add(s);
            }

            // Sunday's Shift Logic: Check the character immediately AFTER the current window
            if (s + m < n) {
                char nextChar = text.charAt(s + m);
                int jumpAmount;

                // Determine shift amount based on that next character
                if (nextChar < 256) {
                    jumpAmount = asciiShifts[nextChar];
                } else {
                    if (unicodeShifts != null && unicodeShifts.containsKey(nextChar)) {
                        jumpAmount = unicodeShifts.get(nextChar);
                    } else {
                        jumpAmount = m + 1; // Character not in pattern, jump past it
                    }
                }
                s += jumpAmount;
            } else {
                s += 1; // End of text reached
            }
        }

        return indicesToString(indices);
    }
}



