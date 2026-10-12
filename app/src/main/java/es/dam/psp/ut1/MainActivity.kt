
package es.dam.psp.ut1

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Process
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import es.dam.psp.ut1.servicio.DescargaService

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                val contexto = this@MainActivity
                val pid = Process.myPid()

                val permisoLauncher =
                    rememberLauncherForActivityResult(
                        ActivityResultContracts.RequestPermission()
                    ) { concedido ->
                        // El usuario ha respondido a la solicitud.
                    }

                LaunchedEffect(Unit) {
                    if (
                        Build.VERSION.SDK_INT >= 33 &&
                        ContextCompat.checkSelfPermission(
                            contexto,
                            Manifest.permission.POST_NOTIFICATIONS
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        permisoLauncher.launch(
                            Manifest.permission.POST_NOTIFICATIONS
                        )
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text("PID de la actividad: $pid")

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(onClick = {
                        val intent = Intent(
                            contexto,
                            DescargaService::class.java
                        )
                        ContextCompat.startForegroundService(
                            contexto,
                            intent
                        )
                    }) {
                        Text("Iniciar")
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(onClick = {
                        val intent = Intent(
                            contexto,
                            DescargaService::class.java
                        )
                        contexto.stopService(intent)
                    }) {
                        Text("Parar")
                    }
                }
            }
        }
    }
}
