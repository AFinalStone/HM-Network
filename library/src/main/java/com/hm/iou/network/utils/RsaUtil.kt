@file:JvmName("RsaUtil")
package com.hm.iou.tools

import android.util.Base64
import java.security.KeyFactory
import java.security.NoSuchAlgorithmException
import java.security.PublicKey
import java.security.spec.InvalidKeySpecException
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import javax.crypto.Cipher

/**
 * 加密算法
 */
private const val ALGORITHM = "RSA"

/**
 * 签名算法
 */
private const val SIGN_ALGORITHMS = "SHA1WithRSA"

/**
 * 采用公钥进行加密
 *
 * @param content 需要加密的字符串
 * @param publicKey 公钥，base64编码
 *
 * @return base64编码后的加密字符串
 */
fun encryptByPublicKey(content: String, publicKey: String): String? {
    return encryptByPublicKey(content.toByteArray(), publicKey)
}

/**
 * 采用公钥进行加密
 *
 * @param content 需要加密的数据
 * @param publicKey 公钥，base64编码
 *
 * @return base64编码后的加密字符串
 */
fun encryptByPublicKey(content: ByteArray, publicKey: String): String? {
    try {
        val pubkey = getPublicKeyFromX509(ALGORITHM, publicKey)
        val cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding")
        cipher.init(Cipher.ENCRYPT_MODE, pubkey)
        val output = cipher.doFinal(content)
        return String(Base64.encode(output, Base64.NO_WRAP))
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return null
}

/**
 *
 * @param algorithm 算法
 * @param bysKey
 * @return
 * @throws InvalidKeySpecException
 * @throws NoSuchAlgorithmException
 */
@Throws(InvalidKeySpecException::class, NoSuchAlgorithmException::class)
private fun getPublicKeyFromX509(algorithm: String, bysKey: String): PublicKey {
    val decodedKey = Base64.decode(bysKey, Base64.NO_WRAP)
    val x509 = X509EncodedKeySpec(decodedKey)
    val keyFactory = KeyFactory.getInstance(algorithm)
    return keyFactory.generatePublic(x509)
}

/**
 * 采用私钥进行解密
 *
 * @param encryptData 加密后的数据，base64编码
 * @param privateKey 私钥，base64编码
 *
 * @return 解密后的字符串
 */
fun decryptByPrivateKey(encryptData: String, privateKey: String): String? {
    return decryptByPrivateKey(Base64.decode(encryptData.toByteArray(), Base64.NO_WRAP), privateKey)
}

/**
 * 采用私钥进行解密
 *
 * @param encryptData 公钥加密后的数据
 * @param privateKey 私钥，base64编码
 *
 * @return 解密后的字符串
 */
fun decryptByPrivateKey(encryptData: ByteArray, privateKey: String): String? {
    try {
        val keyBytes = Base64.decode(privateKey, Base64.NO_WRAP)
        val pkcs8KeySpec = PKCS8EncodedKeySpec(keyBytes)
        val keyFactory = KeyFactory.getInstance(ALGORITHM)
        val privateK = keyFactory.generatePrivate(pkcs8KeySpec)
        val cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding")
        cipher.init(Cipher.DECRYPT_MODE, privateK)
        val result = cipher.doFinal(encryptData)
        return String(result)
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return null
}

/**
 * 采用私钥进行签名
 *
 * @param content 需要签名的内容
 * @param privateKey 私钥，base64编码
 *
 * @return base64编码后的签名字符串
 */
fun signByPrivateKey(content: String, privateKey: String): String? {
    return signByPrivateKey(content.toByteArray(), privateKey)
}

/**
 * 采用私钥进行签名
 *
 * @param content 需要签名的内容
 * @param privateKey 私钥，base64编码
 *
 * @return base64编码后的签名字符串
 */
fun signByPrivateKey(content: ByteArray, privateKey: String): String? {
    try {
        val priPKCS8 = PKCS8EncodedKeySpec(Base64.decode(privateKey, Base64.NO_WRAP))
        val keyf = KeyFactory.getInstance(ALGORITHM)
        val priKey = keyf.generatePrivate(priPKCS8)
        val signature = java.security.Signature.getInstance(SIGN_ALGORITHMS)
        signature.initSign(priKey)
        signature.update(content)
        val signed = signature.sign()
        return String(Base64.encode(signed, Base64.NO_WRAP))
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return null
}

/**
 * 采用公钥验证签名
 *
 * @param content 需要验证的字符串
 * @param sign 签名字符串
 * @param publicKey 公钥, base64编码
 *
 * @return true表示签名一致
 */
fun doCheck(content: String, sign: String, publicKey: String): Boolean {
    return doCheck(content.toByteArray(), sign, publicKey)
}

/**
 * 采用公钥验证签名
 *
 * @param content 需要验证的内容
 * @param sign 签名字符串
 * @param publicKey 公钥, base64编码
 *
 * @return true表示签名一致
 */
fun doCheck(content: ByteArray, sign: String, publicKey: String): Boolean {
    try {
        val keyFactory = KeyFactory.getInstance(ALGORITHM)
        val encodedKey = Base64.decode(publicKey, Base64.NO_WRAP)
        val pubKey = keyFactory.generatePublic(X509EncodedKeySpec(encodedKey))
        val signature = java.security.Signature.getInstance(SIGN_ALGORITHMS)
        signature.initVerify(pubKey)
        signature.update(content)
        return signature.verify(Base64.decode(sign, Base64.NO_WRAP))
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return false
}