package com.example.projekatrazvoj

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.projekatrazvoj.db.DbModule
import com.example.projekatrazvoj.network.RetrofitInstance
import com.example.projekatrazvoj.repository.NewbornRepository
import com.example.projekatrazvoj.repository.DiedRepository
import com.example.projekatrazvoj.ui.MainNavHost
import com.example.projekatrazvoj.ui.theme.ProjekatRazvojTheme
import com.example.projekatrazvoj.viewmodel.NewbornViewModel
import com.example.projekatrazvoj.viewmodel.NewbornViewModelFactory
import com.example.projekatrazvoj.viewmodel.DiedViewModel
import com.example.projekatrazvoj.viewmodel.DiedViewModelFactory
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val context = LocalContext.current
            val db = DbModule.provideDatabase(context)
            val newbornRepository = NewbornRepository(DbModule.provideNewbornApiService(), db.newbornDao())
            val diedRepository = DiedRepository(DbModule.provideDiedApiService(), db.diedDao())
            val newbornViewModel: NewbornViewModel = viewModel(factory = NewbornViewModelFactory(newbornRepository))
            val diedViewModel: DiedViewModel = viewModel(factory = DiedViewModelFactory(diedRepository))
            val datasetTypeState = rememberSaveable { mutableStateOf<String?>(null) }
            if (datasetTypeState.value == null) {
                com.example.projekatrazvoj.ui.OnboardingScreen(onDatasetSelected = { datasetTypeState.value = it })
            } else {
                MainNavHost(
                    newbornViewModel = newbornViewModel,
                    diedViewModel = diedViewModel,
                    datasetType = datasetTypeState.value!!
                )
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ProjekatRazvojTheme {
        Greeting("Android")
    }
}