package de.dlyt.yanndroid.notifer.dialog;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.SwitchCompat;

import de.dlyt.yanndroid.notifer.R;
import de.dlyt.yanndroid.notifer.utils.Crypto;
import de.dlyt.yanndroid.notifer.utils.Preferences;

public class ServerEditDialog {

    public interface ServerEditListener {
        void onAdd(Preferences.ServerInfo serverInfo);

        void onReplaced(Preferences.ServerInfo oldServer);
    }

    private Context mContext;
    private AlertDialog mDialog;

    public ServerEditDialog(Context context, Preferences.ServerInfo serverInfo, ServerEditListener listener) {
        mContext = context;
        View content = LayoutInflater.from(mContext).inflate(R.layout.dialog_edit_server, null);
        AppCompatEditText serverName = content.findViewById(R.id.server_name);
        AppCompatEditText serverUrl = content.findViewById(R.id.server_url);
        SwitchCompat serverInclContent = content.findViewById(R.id.server_incl_content);
        AppCompatEditText serverSecretKey = content.findViewById(R.id.server_secret_key);
        AppCompatImageView serverGenKey = content.findViewById(R.id.server_generate_key);

        if (serverInfo != null) {
            serverName.setText(serverInfo.name);
            serverUrl.setText(serverInfo.url);
            serverInclContent.setChecked(serverInfo.inclContent);
            serverSecretKey.setText(serverInfo.secretKey);
        }

        serverGenKey.setOnClickListener(v -> {
            try {
                String keyB64 = Crypto.generateSecretKeyB64();
                serverSecretKey.setText(keyB64);

                ClipboardManager clipboard = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                clipboard.setPrimaryClip(ClipData.newPlainText("Secret Key", keyB64));

                Toast.makeText(context, R.string.server_secret_key_clipboard, Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Toast.makeText(context, R.string.server_generate_key_failed, Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        });

        mDialog = new AlertDialog.Builder(context)
                .setView(content)
                .setTitle(serverInfo == null ? R.string.server_add : R.string.server_edit)
                .setNegativeButton(dev.oneuiproject.oneui.design.R.string.oui_common_cancel, null)
                .setPositiveButton(dev.oneuiproject.oneui.design.R.string.oui_common_done, (dialog, which) -> {
                    if (serverInfo != null) listener.onReplaced(serverInfo);
                    listener.onAdd(new Preferences.ServerInfo(
                            serverName.getText().toString(),
                            serverUrl.getText().toString(),
                            serverInclContent.isChecked(),
                            serverSecretKey.getText().toString()));
                })
                .create();
    }

    public void show() {
        mDialog.show();
    }

}
