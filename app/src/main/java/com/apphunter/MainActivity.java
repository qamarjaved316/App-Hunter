package com.apphunter;

import android.app.*;
import android.os.*;
import android.content.*;
import android.content.pm.*;
import android.graphics.Color;
import android.view.*;
import android.widget.*;
import java.util.*;

public class MainActivity extends Activity {

    LinearLayout layout;
    ArrayList<ApplicationInfo> suspicious = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(25, 50, 25, 25);
        layout.setBackgroundColor(Color.WHITE);

        TextView title = new TextView(this);
        title.setText("APP HUNTER");
        title.setTextSize(28);
        title.setTextColor(Color.BLACK);
        title.setGravity(Gravity.CENTER);
        layout.addView(title);

        Button scan = new Button(this);
        scan.setText("SCAN MOBILE");
        layout.addView(scan);

        Button detected = new Button(this);
        detected.setText("DETECTED APPS");
        layout.addView(detected);

        setContentView(layout);

        scan.setOnClickListener(v -> scanApps());

        detected.setOnClickListener(v -> showApps());
    }

    void scanApps() {
        suspicious.clear();

        PackageManager pm = getPackageManager();

        for (ApplicationInfo app :
                pm.getInstalledApplications(PackageManager.GET_META_DATA)) {

            if ((app.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                suspicious.add(app);
            }
        }

        Toast.makeText(this,
                "Scan complete: " + suspicious.size() + " apps checked",
                Toast.LENGTH_LONG).show();
    }

    void showApps() {
        if (suspicious.isEmpty()) {
            new AlertDialog.Builder(this)
                    .setMessage("Pehle SCAN MOBILE dabao.")
                    .setPositiveButton("OK", null)
                    .show();
            return;
        }

        String[] names = new String[suspicious.size()];

        for (int i = 0; i < suspicious.size(); i++) {
            names[i] = getPackageManager()
                    .getApplicationLabel(suspicious.get(i)).toString();
        }

        new AlertDialog.Builder(this)
                .setTitle("Detected Apps")
                .setItems(names, (dialog, which) -> {
                    ApplicationInfo app = suspicious.get(which);

                    Intent intent = new Intent(
                            Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    intent.setData(Uri.parse("package:" + app.packageName));
                    startActivity(intent);
                })
                .setNegativeButton("CLOSE", null)
                .show();
    }
}
