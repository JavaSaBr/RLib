package rlib.util.sha160;


public class Sha160 extends BaseHash
{

    private static final int w[] = new int[80];

    private static final synchronized int[] sha(int hh0, int hh1, int hh2, int hh3, int hh4, byte in[], int offset)
    {
        int A = hh0;
        int B = hh1;
        int C = hh2;
        int D = hh3;
        int E = hh4;
        for(int r = 0; r < 16; r++)
            w[r] = in[offset + r * 4] << 24 | (in[offset + r * 4 + 1] & 0xff) << 16 | (in[offset + r * 4 + 2] & 0xff) << 8 | in[offset + r * 4 + 3] & 0xff;

        for(int r = 16; r < 80; r++)
            w[r] = w[r - 3] ^ w[r - 8] ^ w[r - 14] ^ w[r - 16];

        for(int r = 0; r < 20; r++)
        {
            int T = (A << 5 | A >>> 27) + (B & C | ~B & D) + E + w[r] + 0x5a827999;
            E = D;
            D = C;
            C = B << 30 | B >>> 2;
            B = A;
            A = T;
        }

        for(int r = 20; r < 40; r++)
        {
            int T = (A << 5 | A >>> 27) + (B ^ C ^ D) + E + w[r] + 0x6ed9eba1;
            E = D;
            D = C;
            C = B << 30 | B >>> 2;
            B = A;
            A = T;
        }

        for(int r = 40; r < 60; r++)
        {
            int T = (A << 5 | A >>> 27) + (B & C | B & D | C & D) + E + w[r] + 0x8f1bbcdc;
            E = D;
            D = C;
            C = B << 30 | B >>> 2;
            B = A;
            A = T;
        }

        for(int r = 60; r < 80; r++)
        {
            int T = (A << 5 | A >>> 27) + (B ^ C ^ D) + E + w[r] + 0xca62c1d6;
            E = D;
            D = C;
            C = B << 30 | B >>> 2;
            B = A;
            A = T;
        }

        return (new int[] {
            hh0 + A, hh1 + B, hh2 + C, hh3 + D, hh4 + E
        });
    }

    private int h0;

    private int h1;

    private int h2;

    private int h3;

    private int h4;


    public Sha160()
    {
        super("sha-160", 20, 64);
    }
    private Sha160(Sha160 md)
    {
        this();
        h0 = md.h0;
        h1 = md.h1;
        h2 = md.h2;
        h3 = md.h3;
        h4 = md.h4;
        count = md.count;
        buffer = (byte[])(byte[])md.buffer.clone();
    }
    public Object clone()
    {
        return new Sha160(this);
    }
    protected byte[] getResult()
    {
        byte result[] = {
            (byte)h0, (byte)(h0 >>> 8), (byte)(h0 >>> 16), (byte)(h0 >>> 24), (byte)h1, (byte)(h1 >>> 8), (byte)(h1 >>> 16), (byte)(h1 >>> 24), (byte)h2, (byte)(h2 >>> 8),
            (byte)(h2 >>> 16), (byte)(h2 >>> 24), (byte)h3, (byte)(h3 >>> 8), (byte)(h3 >>> 16), (byte)(h3 >>> 24), (byte)h4, (byte)(h4 >>> 8), (byte)(h4 >>> 16), (byte)(h4 >>> 24)
        };
        return result;
    }
    protected byte[] padBuffer()
    {
        int n = (int)(count % 64L);
        int padding = n >= 56 ? 120 - n : 56 - n;
        byte result[] = new byte[padding + 8];
        result[0] = -128;
        long bits = count << 3;
        result[padding++] = (byte)(int)(bits >>> 56);
        result[padding++] = (byte)(int)(bits >>> 48);
        result[padding++] = (byte)(int)(bits >>> 40);
        result[padding++] = (byte)(int)(bits >>> 32);
        result[padding++] = (byte)(int)(bits >>> 24);
        result[padding++] = (byte)(int)(bits >>> 16);
        result[padding++] = (byte)(int)(bits >>> 8);
        result[padding] = (byte)(int)bits;
        return result;
    }
    protected void resetContext()
    {
        h0 = 0x67452301;
        h1 = 0xefcdab89;
        h2 = 0x98badcfe;
        h3 = 0x10325476;
        h4 = 0xc3d2e1f0;
    }
    protected void transform(byte in[], int offset)
    {
        int result[] = sha(h0, h1, h2, h3, h4, in, offset);
        h0 = result[0];
        h1 = result[1];
        h2 = result[2];
        h3 = result[3];
        h4 = result[4];
    }

}