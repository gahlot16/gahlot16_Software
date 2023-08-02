
// REFERENCE
/**
 * Article on Medium -
 * https://apogiatzis.medium.com/shamirs-secret-sharing-a-numeric-example-walkthrough-a59b288c34c4
 * GFG -
 * https://www.geeksforgeeks.org/shamirs-secret-sharing-algorithm-cryptography/
 * Wikipedia - https://en.wikipedia.org/wiki/Shamir%27s_secret_sharing
 * LinkedIn - https://www.linkedin.com/pulse/how-do-pirates-share-treasure-map-dont-trust-each-other-buchanan
 */

import java.math.BigInteger;
import java.util.*;
import java.io.*;

public final class Shamir {

    private BigInteger P;

    private int k;
    private int n;
    private Random random;

    /*
     * certainty a measure of the uncertainty that the caller is willing to
     * tolerate.
     * The probability that the new BigInteger represents a prime number will exceed
     * (1 - 1/2certainty).
     * The execution time of this constructor is proportional to the value of this
     * parameter.
     */
    private static final int CERTAINTY = 10;

    public static String S = "";

    public final class SecretShare {
        public SecretShare(final int num, final BigInteger share) {
            this.num = num;
            this.share = share;
        }

        public int getNum() {
            return num;
        }

        public BigInteger getShare() {
            return share;
        }

        @Override
        public String toString() {
            S += "Secret Share [User ID=" + (num + 1) + ", share=" + share + "]\n";
            return "Secret Share [User ID=" + (num + 1) + ", share=" + share + "]";
        }

        private final int num;
        private final BigInteger share;
    }

    public Shamir(final int k, final int n) {
        this.k = k;
        this.n = n;

        random = new Random();
    }

    public SecretShare[] split(final BigInteger secret) {
        final int modLength = secret.bitLength() + 1;

        // P>S and any any random number.
        P = new BigInteger(modLength, CERTAINTY, random);

        // Coefficient of Equation
        // ai < P && a0 = S
        final BigInteger[] coeff = new BigInteger[k - 1];

        System.out.println("a0: " + secret);

        // ai is a random number less than P
        for (int i = 0; i < k - 1; i++) {
            coeff[i] = randomZp(P);
            System.out.println("a" + (i + 1) + ": " + coeff[i]);
        }

        // Mostly used for printing
        final SecretShare[] shares = new SecretShare[n];
        for (int i = 1; i <= n; i++) {
            BigInteger accum = secret;

            for (int j = 1; j < k; j++) {

                /*
                 * Example -
                 * if n=5, k=3 [j is from 1 to (k-1)]
                 * and i=3, j=2
                 * then
                 * t1 = 3pow2
                 * t2 = (coeff*t1) mod P
                 */
                final BigInteger t1 = BigInteger.valueOf(i).pow(j);
                final BigInteger t2 = coeff[j - 1].multiply(t1).mod(P);

                accum = accum.add(t2).mod(P);
            }

            shares[i - 1] = new SecretShare(i - 1, accum);
            System.out.println(shares[i - 1]);
        }
        return shares;
    }

    public BigInteger getP() {
        return P;
    }

    public BigInteger reconstruction(final SecretShare[] shares, final BigInteger primeNum) {
        BigInteger accum = BigInteger.ZERO;
        S += "\n\nReconstruction....\n";
        for (int i = 0; i < k; i++) {
            BigInteger num = BigInteger.ONE;
            BigInteger den = BigInteger.ONE;

            for (int j = 0; j < k; j++) {
                if (i != j) {
                    num = num.multiply(BigInteger.valueOf(-j - 1)).mod(primeNum);
                    den = den.multiply(BigInteger.valueOf(i - j)).mod(primeNum);
                }
            }
            S += "den: " + den + ", num: " + den + ", inv: " + den.modInverse(primeNum) + "\n";
            // System.out.println("den: " + den + ", num: " + den + ", inv: " +
            // den.modInverse(primeNum));
            final BigInteger value = shares[i].getShare();

            final BigInteger tmp = value.multiply(num).multiply(den.modInverse(primeNum)).mod(primeNum);
            accum = accum.add(primeNum).add(tmp).mod(primeNum);

            S += "value: " + value + ", tmp: " + tmp + ", accum: " + accum + "\n";
            // System.out.println("value: " + value + ", tmp: " + tmp + ", accum: " +
            // accum);
        }

        return accum;
    }

    private BigInteger randomZp(final BigInteger p) {
        while (true) {
            final BigInteger r = new BigInteger(p.bitLength(), random);
            if (r.compareTo(BigInteger.ZERO) > 0 && r.compareTo(p) < 0) {
                return r;
            }
        }
    }

    public static String convertStringToHex(String str) {
        StringBuffer hex = new StringBuffer();

        for (char temp : str.toCharArray()) {
            int decimal = (int) temp;
            hex.append(Integer.toHexString(decimal));
        }

        return hex.toString();
    }

    public static String convertHexToString(String hex) {

        StringBuilder result = new StringBuilder();
        for (int i = 0; i < hex.length() - 1; i += 2) {
            String tempInHex = hex.substring(i, (i + 2));
            int decimal = Integer.parseInt(tempInHex, 16);
            result.append((char) decimal);
        }
        return result.toString();
    }

    public static String FileReader() {
        String plain = "";
        try {
            FileReader reader = new FileReader("file.txt");
            int i;
            while ((i = reader.read()) != -1) {
                plain += (char) i;
            }
            reader.close();
            FileWriter writer = new FileWriter("file.txt");
            writer.write("");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Text file read.");
        return plain;
    }

    public static void plainTextWriter(String result) {
        try {
            File plaintext = new File("Output.txt");
            System.out.println("File created: " + plaintext.getName());

            String plain = result;
            FileWriter writer = new FileWriter(plaintext);
            writer.write(plain);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void sharesWriter(String shares) {
        try {
            File share = new File("Details.txt");
            System.out.println("File created: " + share.getName());

            String plain = shares;
            FileWriter writer = new FileWriter(share);
            writer.write(plain);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int userVerifier() {
        int count = 0;
        try {
            Scanner sc = new Scanner(System.in);
            boolean cond = true;

            while (count <= n && cond == true) {
                System.out.print("Enter your user ID:");
                int i = sc.nextInt();
                if (i <= n) {
                    count++;
                } else {
                    System.out.println("Not a Valid User ID!!");
                }

                System.out.println("More Users? (y/n)");
                char c = (sc.next().charAt(0));
                if (c == 'y' || c == 'Y') {
                    continue;
                } else {
                    cond = false;
                }
            }
        } catch (Exception e) {
            System.out.println("Some Error Occured");
        }
        return count;
    }

    public static void main(final String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Number of persons in a group (N): ");
        int N = sc.nextInt();
        System.out.print("Number of minimum person required to unlock (K): ");
        int K = sc.nextInt();

        final Shamir shamir = new Shamir(K, N);

        // String Initialisation
        String text = FileReader();
        // Converting String to Hex
        String hex = convertStringToHex(text);

        // Converting Hex to Decimal.
        final BigInteger secret = new BigInteger(hex, 16);

        // Creating Shares by spliting the shares accordingly to the sahre holders.
        final SecretShare[] shares = shamir.split(secret);

        System.out.println("||||||||||||||||||||||||||||||||||||||||||||||||||||");
        System.out.println("||||||||||||||||||||||||||||||||||||||||||||||||||||");
        int users = shamir.userVerifier();
        System.out.println("||||||||||||||||||||||||||||||||||||||||||||||||||||");

        final BigInteger prime = shamir.getP();

        if (users >= K) {
            // Recunstruting the shares using Lagrange Polynomial Interpolation theorem.
            final Shamir shamir2 = new Shamir(K, N);
            //Use result as a key in ARS encryption.
            final BigInteger result = shamir2.reconstruction(shares, prime);
            String plaintext = convertHexToString(result.toString(16));
            System.out.println("Decoded Secret is stored in: ");
            // Writing the plaintext in a file.
            plainTextWriter(plaintext);
        } else {
            S += "\nThere were unsufficient users, so file cannot be opened.";
            System.out.println("NOT SUFFICIENT USERS!!!");
        }

        // writing all the shares in a file
        sharesWriter(S);
        S = "";
    }

}
