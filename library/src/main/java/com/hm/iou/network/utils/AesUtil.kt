@file: JvmName("AesUtil")

package com.hm.iou.network.utils

import android.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

/**
 * Created by hjy on 2019/91/9
 *
 * AES 加解密工具类
 *
 */


const val KEY_CHARS = "0123456789abcdefghijklmnopqrstuvwxyz"

/**
 * 生成 AES 加密的 key
 */
fun generateRandomKey(): String {
    val sb = StringBuilder()
    for (i in 0..15) {
        sb.append(KEY_CHARS[(Math.random() * 36).toInt()])
    }
    return sb.toString()
}

/**
 * 通过 AES 加密
 *
 * @param content   要加密的明文
 * @param key
 *
 * @return base64字符串，如果失败则返回null
 */
fun encryptEcb(content: String, key: String): String? {
    try {
        val newkey = ByteArray(16)
        var i = 0
        val pwdByteArr = key.toByteArray()
        while (i < newkey.size && i < pwdByteArr.size) {
            newkey[i] = pwdByteArr[i]
            ++i
        }
        val key = SecretKeySpec(newkey, "AES")
        val cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
        val byteContent = content.toByteArray()
        cipher.init(Cipher.ENCRYPT_MODE, key)
        val result = cipher.doFinal(byteContent)
        return String(Base64.encode(result, Base64.NO_WRAP))
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return null
}

/**
 * 通过 AES 解密
 *
 * @param encrypted base64加密字符串
 * @param key
 *
 * @return 解密后的字符串，如果失败则返回null
 */
fun decryptECB(encrypted: String, key: String): String? {
    try {
        val newkey = ByteArray(16)
        var i = 0
        val pwdByteArr = key.toByteArray()
        while (i < newkey.size && i < pwdByteArr.size) {
            newkey[i] = key.toByteArray()[i]
            ++i
        }

        val key = SecretKeySpec(newkey, "AES")
        val cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
        cipher.init(Cipher.DECRYPT_MODE, key)
        val bytes = cipher.doFinal(Base64.decode(encrypted, Base64.NO_WRAP))
        return String(bytes)
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return null
}