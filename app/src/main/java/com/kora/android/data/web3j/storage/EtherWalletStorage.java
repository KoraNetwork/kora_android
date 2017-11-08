package com.kora.android.data.web3j.storage;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;

import com.kora.android.data.web3j.model.EtherWallet;

import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import static com.kora.android.common.Keys.EXPORT_FOLDER_NAME;
import static com.kora.android.common.Keys.JSON_FILE_EXTENSION;
import static com.kora.android.common.Keys.WALLET_LIST_FILE_NAME;

public class EtherWalletStorage {

    private final Context mContext;

    public EtherWalletStorage(final Context context) {
        this.mContext = context;
    }

    public List<EtherWallet> getWalletList() {
        List<EtherWallet> etherWalletList;
        File file;
        FileInputStream fileInputStream = null;
        BufferedInputStream bufferedInputStream = null;
        ObjectInputStream objectInputStream = null;
        try {
            file = new File(mContext.getFilesDir(), WALLET_LIST_FILE_NAME);
            fileInputStream = new FileInputStream(file);
            bufferedInputStream = new BufferedInputStream(fileInputStream);
            objectInputStream = new ObjectInputStream(bufferedInputStream);
        } catch (Exception ignored) {
        }
        try {
            etherWalletList = (ArrayList<EtherWallet>) objectInputStream.readObject();
        } catch (Exception e) {
            etherWalletList = new ArrayList<>();
            Log.e("_____", e.toString());
            e.printStackTrace();
        }
        try {
            if (objectInputStream != null)
                objectInputStream.close();
            if (bufferedInputStream != null)
                bufferedInputStream.close();
            if (fileInputStream != null)
                fileInputStream.close();
        } catch (Exception ignored) {
        }
        return etherWalletList;
    }

    public void addWallet(final EtherWallet etherWallet) {
        final List<EtherWallet> etherWalletList = getWalletList();
        if (etherWalletList.contains(etherWallet))
            return;
        etherWalletList.add(etherWallet);
        saveWalletList(etherWalletList);
    }

    public void deleteWallets() {
        final List<EtherWallet> etherWalletList = getWalletList();
        if (etherWalletList.isEmpty())
            return;
        try {
            for (int i = 0; i < etherWalletList.size(); i++) {
                final EtherWallet etherWallet = etherWalletList.get(i);
                final File file = new File(mContext.getFilesDir(), etherWallet.getWalletFileName());
                file.delete();
            }
        } catch (Exception e) {
            Log.e("_____", e.toString());
            e.printStackTrace();
        }
        etherWalletList.clear();
        saveWalletList(etherWalletList);
    }

    private void saveWalletList(List<EtherWallet> etherWalletList) {
        File file;
        FileOutputStream fileOutputStream = null;
        ObjectOutputStream objectOutputStream = null;
        try {
            file = new File(mContext.getFilesDir(), WALLET_LIST_FILE_NAME);
            fileOutputStream = new FileOutputStream(file);
            objectOutputStream = new ObjectOutputStream(fileOutputStream);
        } catch (Exception ignored) {
        }
        try {
            objectOutputStream.writeObject(etherWalletList);
        } catch (Exception e) {
            Log.e("_____", e.toString());
            e.printStackTrace();
        }
        try {
            if (objectOutputStream != null)
                objectOutputStream.close();
            if (fileOutputStream != null)
                fileOutputStream.close();
        } catch (Exception ignored) {
        }
    }

    public void exportWallet(final String walletFileName) throws IOException {
        final List<EtherWallet> etherWalletList = getWalletList();
        final EtherWallet etherWallet = EtherWallet.createEtherWalletFromFileName(walletFileName);
        if (!etherWalletList.contains(etherWallet))
            return;
        final File folder = new File(Environment.getExternalStorageDirectory(), EXPORT_FOLDER_NAME);
        if (!folder.exists())
            folder.mkdirs();
        final File original = new File(mContext.getFilesDir(), walletFileName);
        final File copy = new File(folder, walletFileName + JSON_FILE_EXTENSION);

        copyFile(original, copy);

        final Intent mediaScannerIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        final Uri fileUri = Uri.fromFile(copy);
        mediaScannerIntent.setData(fileUri);
        mContext.sendBroadcast(mediaScannerIntent);
    }

    public void copyFile(final File original, final File copy) throws IOException {
        FileChannel inChannel = null;
        FileChannel outChannel = null;
        try {
            inChannel = new FileInputStream(original).getChannel();
            outChannel = new FileOutputStream(copy).getChannel();
        } catch (Exception ignored) {
        }

        inChannel.transferTo(0, inChannel.size(), outChannel);

        try {
            if (inChannel != null)
                inChannel.close();
            if (outChannel != null)
                outChannel.close();
        } catch (Exception ignored) {
        }
    }

    public void copyFile(final Uri originalUri, final File copy) throws IOException {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            inputStream = mContext.getContentResolver().openInputStream(originalUri);
            outputStream = new FileOutputStream(copy);
        } catch (Exception ignored) {
        }

        final byte[] buffer = new byte[1024];
        int read;
        while ((read = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, read);
        }

        try {
            if (inputStream != null)
                inputStream.close();
            if (outputStream != null)
                outputStream.close();
        } catch (Exception ignored) {
        }
    }

    public Credentials getCredentials(final String walletFileName, final String password) throws IOException, CipherException {
        final File file = new File(mContext.getFilesDir(), walletFileName);
        return WalletUtils.loadCredentials(password, file);
    }

    public FileMetaData getFileMetaData(final Context context, final Uri uri) {
        final FileMetaData fileMetaData = new FileMetaData();
        if ("file".equalsIgnoreCase(uri.getScheme())) {
            final File file = new File(uri.getPath());
            fileMetaData.setDisplayName(file.getName());
            fileMetaData.setSize(file.length());
            fileMetaData.setPath(file.getPath());
            return fileMetaData;
        } else {
            final ContentResolver contentResolver = context.getContentResolver();
            final Cursor cursor = contentResolver.query(uri, null, null, null, null);
            fileMetaData.setMimeType(contentResolver.getType(uri));
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    int sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE);
                    fileMetaData.setDisplayName(cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)));
                    if (!cursor.isNull(sizeIndex))
                        fileMetaData.setSize(cursor.getLong(sizeIndex));
                    else
                        fileMetaData.setSize(-1);
                    try {
                        fileMetaData.setPath(cursor.getString(cursor.getColumnIndexOrThrow("_data")));
                    } catch (final Exception exception) {
                        // do nothing, data does not exist
                    }
                    return fileMetaData;
                }
            } catch (Exception exception) {
                Log.e("_____", exception.toString());
            } finally {
                if (cursor != null)
                    cursor.close();
            }
            return null;
        }
    }

    public String getUriPath(final Context context, final Uri uri) {
        String path;
        final String[] projection = {MediaStore.Files.FileColumns.DATA};
        final Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null) {
            path = uri.getPath();
        } else {
            cursor.moveToFirst();
            int column_index = cursor.getColumnIndexOrThrow(projection[0]);
            path = cursor.getString(column_index);
            cursor.close();
        }
        return ((path == null || path.isEmpty()) ? (uri.getPath()) : path);
    }
}
