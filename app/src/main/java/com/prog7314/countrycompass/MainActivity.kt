package com.prog7314.countrycompass

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.prog7314.countrycompass.data.Country
import com.prog7314.countrycompass.ui.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { MaterialTheme { CountryCompassApp() } }
    }
}

@Composable
fun CountryCompassApp(vm: CountryViewModel = viewModel()) {
    val nav = rememberNavController()
    NavHost(navController = nav, startDestination = "search") {
        composable("search") {
            SearchScreen(vm) { country, query ->
                nav.navigate("detail/${android.net.Uri.encode(query)}")
            }
        }
        composable(
            "detail/{identifier}",
            arguments = listOf(navArgument("identifier") { type = NavType.StringType })
        ) { entry ->
            val identifier = entry.arguments?.getString("identifier").orEmpty()
            DetailScreen(vm, identifier, onBack = { nav.popBackStack() }) { border ->
                nav.navigate("detail/${android.net.Uri.encode(border)}")
            }
        }
    }
}

@Composable
private fun SearchScreen(vm: CountryViewModel,onCountry: (Country, String) -> Unit) {
    var query by remember { mutableStateOf("") }
    val state by vm.searchState.collectAsStateWithLifecycle()
    Column(Modifier.fillMaxSize().padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Country Compass", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Text("Search for a country and explore its neighbours.")
        OutlinedTextField(query, { query = it }, label = { Text("Country name") }, modifier = Modifier.fillMaxWidth())
        Button(onClick = { vm.search(query) }, modifier = Modifier.fillMaxWidth()) { Text("Search") }
        when (val current = state) {
            SearchState.Idle -> Text("Try South Africa, Congo or Guinea.")
            SearchState.Loading -> CircularProgressIndicator()
            is SearchState.Error -> Text(current.message)
            is SearchState.Success -> LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(current.countries, key = { it.codes.alpha3 }) { country ->
                    Card(Modifier.fillMaxWidth().clickable { onCountry(country, query) }) {
                        Column(Modifier.padding(16.dp)) {
                            Text(country.names.common, fontWeight = FontWeight.Bold)
                            Text(country.names.official)
                            Text("${country.region} • ${country.codes.alpha3}")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DetailScreen(vm: CountryViewModel, identifier: String, onBack: () -> Unit, onBorder: (String) -> Unit) {
    val state by vm.detailState.collectAsStateWithLifecycle()
    LaunchedEffect(identifier) { vm.loadCountry(identifier) }
    Column(Modifier.fillMaxSize().padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        TextButton(onClick = onBack) { Text("Back") }
        when (val current = state) {
            DetailState.Loading -> CircularProgressIndicator()
            is DetailState.Error -> Text(current.message)
            is DetailState.Success -> {
                val c = current.country
                Text(c.names.common, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                Text(c.names.official)
                Text("Code: ${c.codes.alpha3}")
                Text("Capital: ${c.capitals.firstOrNull()?.name ?: "Not listed"}")
                Text("Region: ${c.region}")
                Text("Population: ${"%,d".format(c.population)}")
                Text("Borders", fontWeight = FontWeight.Bold)
                if (c.borders.isEmpty()) Text("No land borders") else c.borders.forEach { code ->
                    OutlinedButton(onClick = { onBorder(code) }, modifier = Modifier.fillMaxWidth()) { Text(code) }
                }
            }
        }
    }
}
