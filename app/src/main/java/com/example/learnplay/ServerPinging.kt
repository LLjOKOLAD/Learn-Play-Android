package com.example.learnplay


import android.os.AsyncTask
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class ServerPinging : AppCompatActivity() {

    private lateinit var button: Button
    private lateinit var textView: TextView
    private lateinit var ipadress: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_server_pinging)

        button = findViewById(R.id.pingServ)
        textView = findViewById(R.id.servText)
        ipadress = findViewById(R.id.ipName)

        button.setOnClickListener {
            val ip_ad = ipadress.text.toString()
            FetchDataTask(textView, ip_ad).execute()
        }
    }




    class FetchDataTask(private val textView: TextView, private val ip_ad: String) : AsyncTask<Void, Void, String>() {
        override fun doInBackground(vararg params: Void?): String {
            var result = ""
            try {

                val url = URL(ip_ad)
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"
                val responseCode = connection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    val reader = BufferedReader(InputStreamReader(connection.inputStream))
                    val response = StringBuilder()
                    var inputLine: String?
                    while (reader.readLine().also { inputLine = it } != null) {
                        response.append(inputLine)
                    }
                    reader.close()
                    result = response.toString()
                } else {
                    result = "Error: $responseCode"
                }
            } catch (e: Exception) {
                e.printStackTrace()
                result = "Error: ${e.message}"
            }
            return result
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            if (result != null) {
                val task = Gson().fromJson(result, Task::class.java)
                val stringBuilder = StringBuilder()
                stringBuilder.append("idTask: ${task.idTask}\n")
                stringBuilder.append("idTopic: ${task.idTopic}\n")
                stringBuilder.append("quotation: ${task.quotation}\n")
                stringBuilder.append("addText: ${task.addText}\n")
                stringBuilder.append("answer: ${task.answer}\n")
                stringBuilder.append("exp: ${task.exp}\n")
                stringBuilder.append("image: ${task.image}\n")

                textView.text = stringBuilder.toString()
            }
        }

    }
}
