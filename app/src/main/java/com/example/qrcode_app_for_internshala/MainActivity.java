package com.example.qrcode_app_for_internshala;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MainActivity extends AppCompatActivity {
    private Button scan_btn;
    private TextView textView;
    private WebView webView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scan_btn = findViewById(R.id.scanner);
        textView = findViewById(R.id.text);
        webView = findViewById(R.id.webView);

        webView.setVisibility(View.GONE);

        scan_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new IntentIntegrator(MainActivity.this)
                        .setOrientationLocked(true)
                        .setPrompt("Scan a QR Code")
                        .setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
                        .initiateScan();
            }
        });

        webView.setWebViewClient(new WebViewClient());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (intentResult != null) {
            if (intentResult.getContents() != null) {
                String contents = intentResult.getContents();
                textView.setText(contents);

                // Check if the scanned content is a valid URL
                if (isURL(contents)) {
                    webView.setVisibility(View.VISIBLE);
                    webView.loadUrl(contents);
                } else {
                    webView.setVisibility(View.GONE);
                    Toast.makeText(this, "Not a valid URL", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Scan canceled", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Check if a string is a valid URL
    private boolean isURL(String text) {
        try {
            Uri uri = Uri.parse(text);
            return "http".equals(uri.getScheme()) || "https".equals(uri.getScheme());
        } catch (Exception e) {
            return false;
        }
    }
}
