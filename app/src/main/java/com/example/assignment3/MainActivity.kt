package com.example.assignment3

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.outlined.Call
import androidx.compose.material.icons.outlined.Done
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.assignment3.ui.theme.Assignment3Theme

class MainActivity : ComponentActivity() {
    private val viewModel: ContactViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Assignment3Theme {
                // A surface container using the 'background' color from the theme
                val contacts by viewModel.contacts.observeAsState(initial = emptyList())
                // UI content
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {


                        Spacer(modifier = Modifier.height(16.dp))

                        // Button to trigger contact import
                        Button(onClick = { onImportContactsClick() }) {
                            Text("Import Contacts")
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Display the contact list
                        val selectedContact by viewModel.selectedContact.observeAsState()
                        ContactListScreen(
                            contacts = contacts,
                            viewModel = viewModel,
                            onContactClick = { contact ->
                                onContactClick(
                                    contact,
                                    selectedContact
                                )
                            },
                            onUpdateClick = { updatedContact ->
                                viewModel.updateContact(
                                    updatedContact
                                )
                            },
                            onDeleteClick = { selectedContact?.let { viewModel.deleteContact(it) } },
                            onCallClick = { contact -> onCallClick(contact.phoneNo) },
                            onMessageClick = { contact -> onMessageClick(contact.phoneNo) }
                        )
                    }
                }
            }
        }
    }

    private fun onImportContactsClick() {
        // Assuming you have obtained the ContentResolver appropriately
        viewModel.importContacts(contentResolver, this)
    }

    private fun onContactClick(contact: Contact, selectedContact: Contact?) {
        // Update the selected contact when a contact is clicked
        // Do any additional logic here if needed
        viewModel.setSelectedContact(contact)
    }

    private fun onCallClick(phoneNumber: String) {
        // Create an intent to dial the phone number
        val callIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phoneNumber"))
        startActivity(callIntent)
    }

    private fun onMessageClick(phoneNumber: String) {
        // Create an intent to open the messaging app
        val messageIntent = Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:$phoneNumber"))
        startActivity(messageIntent)
    }
}

@Composable
fun ContactListScreen(
    contacts: List<Contact>,
    viewModel: ContactViewModel,
    onContactClick: (Contact) -> Unit,
    onUpdateClick: (Contact) -> Unit,
    onDeleteClick: () -> Unit,
    onCallClick: (Contact) -> Unit,
    onMessageClick: (Contact) -> Unit
) {
    Surface(modifier = Modifier.fillMaxSize()) {
        val selectedContact by viewModel.selectedContact.observeAsState()

        LazyColumn {
            items(contacts) { contact ->
                ContactListItem(
                    contact = contact,
                    onContactClick = onContactClick,
                    onUpdateClick = onUpdateClick,
                    onDeleteClick = onDeleteClick,
                    onCallClick = onCallClick,
                    onMessageClick = onMessageClick
                )
                Divider() // Add a divider between items for better separation
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactListItem(
    contact: Contact,
    onContactClick: (Contact) -> Unit,
    onUpdateClick: (Contact) -> Unit,
    onDeleteClick: () -> Unit,
    onCallClick: (Contact) -> Unit,
    onMessageClick: (Contact) -> Unit
) {
    var isEditing by remember { mutableStateOf(false) }
    var updatedName by remember { mutableStateOf(contact.name) }
    var updatedPhone by remember { mutableStateOf(contact.phoneNo) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onContactClick(contact) },



        ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()

        ) {
            Text(text = "Contact", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Spacer(modifier = Modifier.height(4.dp))

            // Display contact details
            Text(text = "Name: ${if (isEditing) updatedName else contact.name}", fontSize = 16.sp)
            Text(text = "Phone: ${if (isEditing) updatedPhone else contact.phoneNo}", fontSize = 16.sp)

            // Editable text fields when editing is enabled
            if (isEditing) {
                OutlinedTextField(
                    value = updatedName,
                    onValueChange = { updatedName = it },
                    label = { Text("Updated Name") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )

                OutlinedTextField(
                    value = updatedPhone,
                    onValueChange = { updatedPhone = it },
                    label = { Text("Updated Phone") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )
            }
        }

        // Buttons for actions
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(8.dp),

            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Replace the FloatingActionButton with IconButton for better styling
            IconButton(onClick = {
                isEditing = !isEditing
                if (!isEditing) {
                    onUpdateClick(Contact(contact.id, updatedName, updatedPhone))
                }
            }) {
                Icon(
                    imageVector = if (isEditing) Icons.Outlined.Done else Icons.Default.Edit,
                    contentDescription = if (isEditing) "Save" else "Edit"
                )
            }

            // Replace FloatingActionButton with IconButton for better styling
            IconButton(onClick = { onDeleteClick() }) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete")
            }

            // Replace FloatingActionButton with IconButton for better styling
            IconButton(onClick = { onCallClick(contact) }) {
                Icon(imageVector = Icons.Outlined.Call, contentDescription = "Call")
            }

            // Replace FloatingActionButton with IconButton for better styling
            IconButton(onClick = { onMessageClick(contact) }) {
                Icon(imageVector = Icons.Outlined.Email, contentDescription = "Msg")
            }
        }
    }
}
