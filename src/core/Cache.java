import java.math.BigInteger;
import java.io.File;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Cache {
  private final String CACHE_DIR_PREFIX = ".cache";
  private final String key_hash;
  private final String key;

  public Cache(String key) throws Exception {
    this.key = key;
    this.key_hash = getKeyHash();
  }

  public void clean(File file) throws IOException {
    if (file.isDirectory()) {
      for (File c : file.listFiles()) {
        clean(c);
      }
    }

    if (!file.delete()) {
      throw new FileNotFoundException("Failed to delete file: " + file);
    }
  }

  public String getCachePath() {
    return this.CACHE_DIR_PREFIX + File.separator + this.key_hash;
  }

  public void init() {
    File cache_dir = new File(getCachePath());

    if (!cache_dir.exists()) {
      cache_dir.mkdirs();
    }
  }

  private String getKeyHash() throws UnsupportedEncodingException, NoSuchAlgorithmException {
    if (this.key_hash != null && !this.key_hash.isBlank()) {
      return this.key_hash;
    }

    byte[] key_bytes = key.getBytes("UTF-8");
    MessageDigest md = MessageDigest.getInstance("MD5");
    byte[] hash_digest = md.digest(key_bytes);
    BigInteger big_int = new BigInteger(1, hash_digest);
    String hash_string = big_int.toString(16);

    while(hash_string.length() < 32){
      hash_string = "0" + hash_string;
    }

    return hash_string;
  }
}

