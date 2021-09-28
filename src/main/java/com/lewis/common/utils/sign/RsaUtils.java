package com.lewis.common.utils.sign;

import com.lewis.common.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 * java 使用 jsencrypt 的 js 的 rsa 库实现 rsa 加密传输，防止 http 明文传输
 *
 * @author lewis
 */
@Slf4j
public class RsaUtils {

	public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeySpecException {
		// 模拟前端 jsencrypt 使用公钥加密的过程
		String nonce = Math.random() * 100000 + "";
		String timestamp = String.valueOf(DateUtils.current());
		// 加密使用的公钥
		String publicKey = "     MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCKi/lsc9QmECiCC9j6QV3Jnt9j\n" +
				"     qtRdQltwyyjFycZ58tOJFe7ps/XrDek7RaKQUlRL3dfU2uP1XAZFPg4vn6wfosB0\n" +
				"     ny3PKdOcrS8pbSUQj1tJT/USesRVCY51jrv1xb3hiQr6FqD3yjC9/VImHlKYWRyy\n" +
				"     ngJjXQSsV0XhZ7++jwIDAQAB\n".replace("\n", "");
		// 需要加密的内容
		String decodedData = nonce + "_" + timestamp;
		// 加密的过程
		String encodedData = RsaUtils.publicEncrypt(decodedData, RsaUtils.getPublicKey(publicKey));
		log.info("加密后文字: - {}", encodedData);

		// 使用私钥解密的demo
		String privateKey = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAMKz0Z18guQrLPdC\n" +
				"JdtPK0vR7SpeBie0LBELf7atQYyKJ3WgX6CNGmrFDlsQWp17X0T/s9KzaHbpc2I2\n" +
				"PvlZoBz7CHnfDGwi7p/sI9dVNeOdfFa5drsqmK60rGrj6BzveGgPDJJoXPb5Lbfb\n" +
				"MDN34hG/QGfuDsEkfUbYYB+apC0ZAgMBAAECgYEAuFiPWGBCggyLJ5T+yPXdlY0u\n" +
				"05VwmHkT3BOaGXlTfeB02f89a4MOBxeKrxf94+ui2W6NcSqi9yu0LsITv/1nBUK4\n" +
				"oBOjC75garFjYjSDTdGLD96BDOahVzqKMY8eE9XEZQEX4bhj6ZDa5q8nvGnkant0\n" +
				"o5yZzGeT2ZMK69SMdjECQQDfR2TBcNHhJLp3I8qzuV8iYsZnyUEU+I47DRRv9qKl\n" +
				"1WWAW3EJ7cyv69OkAQHwJy2BDQDuSFgn6T/lx4N1yCmlAkEA3zxXROmK5T7shE1H\n" +
				"Ymt3AdTSFnAcR/lKbd/+iuIhaN77MOzEaKvlDxRv5Jh4G+lLkwHFSAaRYRPmkv6q\n" +
				"mBqTZQJBAN3uA6r2rdaggCrty4wqg/IUxerhMqxahl0Rmi/TsUUuQA5+VXQuBpcR\n" +
				"y7KnQbrn5iXwu+0cwWsiP93wGq3Wv/UCQCvw0Ky7258sN5oDLB3vUUmG/qN0Bd0U\n" +
				"8NWX1Z64zCK8YW1L7Y086KWDPFMev+WekkWpf4+h21PkeupMPoAaGxECQGDnx0/U\n" +
				"A972Y4wZfP+jjCG74UdqSH51+FRrLOzLUuhvQSiKYmSpZXpWzOKa0ZnmAuIfjWIe\n" +
				"n/VmjuQ6iaLiWR0=".replace("\n", "");
		// 需要解密的内容
		encodedData = "jJlRUw9JXFtLvO3dLOTQlFu9oHVnJsfi0sZ0u9BbEZMj8IlsPvSA95eExRCpe060VFaCEc7j17tm4Qpw2kX0dDg71DoPPYioT06Nhwf2BkNghtKkcWhDr8HJo85aIN2MAWSk4B/kpsvaUDuazJV7QyKaMK80UtQ/ZhzGvkR0AnI=";
		// 解密后的内容
		decodedData = RsaUtils.privateDecrypt(encodedData, RsaUtils.getPrivateKey(privateKey));
		System.out.println("解密后文字: \r\n" + decodedData);
	}

	public static final String CHARSET = "UTF-8";

	public static final String RSA_ALGORITHM = "RSA";

	/**
	 * 构建一对公钥和私钥
	 *
	 * @param keySize 密钥长度
	 * @return 公钥和私钥的 map
	 */
	public static Map<String, String> createKeys(int keySize) {
		//为RSA算法创建一个KeyPairGenerator对象
		KeyPairGenerator kpg;
		try {
			kpg = KeyPairGenerator.getInstance(RSA_ALGORITHM);
			//初始化KeyPairGenerator对象,密钥长度
			kpg.initialize(keySize);
			//生成密匙对
			KeyPair keyPair = kpg.generateKeyPair();
			//得到公钥
			Key publicKey = keyPair.getPublic();
			String publicKeyStr = Base64.encodeBase64URLSafeString(publicKey.getEncoded());
			//得到私钥
			Key privateKey = keyPair.getPrivate();
			log.info("私钥格式 - {}", privateKey.getFormat());
			String privateKeyStr = Base64.encodeBase64URLSafeString(privateKey.getEncoded());
			Map<String, String> keyPairMap = new HashMap<String, String>();
			keyPairMap.put("publicKey", publicKeyStr);
			keyPairMap.put("privateKey", privateKeyStr);

			return keyPairMap;
		} catch (NoSuchAlgorithmException e) {
			log.error("No such algorithm - {}", RSA_ALGORITHM);
			return null;
		}
	}

	/**
	 * 通过公钥字符串获取 RSAPublicKey 对象
	 *
	 * @param publicKey 密钥字符串（RSAPublicKey对象，经过base64编码）
	 */
	public static RSAPublicKey getPublicKey(String publicKey) {
		//通过X509编码的Key指令获得公钥对象
		KeyFactory keyFactory = null;
		try {
			keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
			X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(Base64.decodeBase64(publicKey));
			return (RSAPublicKey) keyFactory.generatePublic(x509KeySpec);
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 通过私钥字符串获取 RSAPublicKey 对象
	 *
	 * @param privateKey 密钥字符串（经过base64编码）
	 */
	public static RSAPrivateKey getPrivateKey(String privateKey) {
		//通过PKCS#8编码的Key指令获得私钥对象
		KeyFactory keyFactory = null;
		try {
			keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
			PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(Base64.decodeBase64(privateKey));
			return (RSAPrivateKey) keyFactory.generatePrivate(pkcs8KeySpec);
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 使用公钥加密
	 *
	 * @param data 被加密的内容
	 * @param publicKey 提供加密的公钥
	 * @return 加密后的字符串
	 */
	public static String publicEncrypt(String data, RSAPublicKey publicKey) {
		try {
			Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			return Base64.encodeBase64URLSafeString(rsaSplitCodec(cipher, Cipher.ENCRYPT_MODE, data.getBytes(CHARSET), publicKey.getModulus().bitLength()));
		} catch (Exception e) {
			log.error("加密字符串[" + data + "]时遇到异常 - {}", e);
			return null;
		}
	}

	/**
	 * 使用私钥解密
	 *
	 * @param data       需要解密的内容
	 * @param privateKey 解密使用的私钥
	 * @return 解密后的字符串
	 */
	public static String privateDecrypt(String data, RSAPrivateKey privateKey) {
		try {
			Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			return new String(rsaSplitCodec(cipher, Cipher.DECRYPT_MODE, Base64.decodeBase64(data), privateKey.getModulus().bitLength()), CHARSET);
		} catch (Exception e) {
			log.error("解密字符串[" + data + "]时遇到异常 - {}", e);
			return null;
		}
	}

	/**
	 * 使用私钥加密
	 *
	 * @param data       需要加密的内容
	 * @param privateKey 加密使用的私钥
	 * @return 加密后的字符串
	 */
	public static String privateEncrypt(String data, RSAPrivateKey privateKey) {
		try {
			Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
			cipher.init(Cipher.ENCRYPT_MODE, privateKey);
			return Base64.encodeBase64URLSafeString(rsaSplitCodec(cipher, Cipher.ENCRYPT_MODE, data.getBytes(CHARSET), privateKey.getModulus().bitLength()));
		} catch (Exception e) {
			log.error("加密字符串[" + data + "]时遇到异常 - {}", e);
			return null;
		}
	}

	/**
	 * 使用公钥解密
	 *
	 * @param data 被解密的内容
	 * @param publicKey 解密使用的公钥
	 * @return 公钥解密后的字符串
	 */
	public static String publicDecrypt(String data, RSAPublicKey publicKey) {
		try {
			Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
			cipher.init(Cipher.DECRYPT_MODE, publicKey);
			return new String(rsaSplitCodec(cipher, Cipher.DECRYPT_MODE, Base64.decodeBase64(data), publicKey.getModulus().bitLength()), CHARSET);
		} catch (Exception e) {
			log.error("解密字符串[" + data + "]时遇到异常 - {}", e);
			return null;
		}
	}

	private static byte[] rsaSplitCodec(Cipher cipher, int opmode, byte[] datas, int keySize) {
		int maxBlock = 0;
		if (opmode == Cipher.DECRYPT_MODE) {
			maxBlock = keySize / 8;
		} else {
			maxBlock = keySize / 8 - 11;
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int offSet = 0;
		byte[] buff;
		int i = 0;
		try {
			while (datas.length > offSet) {
				if (datas.length - offSet > maxBlock) {
					buff = cipher.doFinal(datas, offSet, maxBlock);
				} else {
					buff = cipher.doFinal(datas, offSet, datas.length - offSet);
				}
				out.write(buff, 0, buff.length);
				i++;
				offSet = i * maxBlock;
			}
		} catch (Exception e) {
			throw new RuntimeException("加解密阀值为[" + maxBlock + "]的数据时发生异常", e);
		}
		byte[] resultDatas = out.toByteArray();
		IOUtils.closeQuietly(out);
		return resultDatas;
	}

}

