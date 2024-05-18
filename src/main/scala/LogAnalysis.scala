import java.io.{File, PrintWriter}
import org.apache.spark.sql.SparkSession

object LogAnalysis {
  def main(args: Array[String]): Unit = {
    // Initialisation de la session Spark
    val spark = SparkSession.builder()
      .appName("LogAnalysis")
      .master("local") // Utilisation de Spark en mode local
      .getOrCreate()

    import spark.implicits._

    // Chargement du fichier de logs Apache
    val logData = spark.read.textFile("C:\\xampp\\apache\\logs\\access.log")

    // Analyse des erreurs courantes
    // Cette étape filtre les lignes contenant "ERROR" et compte le nombre d'erreurs trouvées.
    val errorCount = logData.filter(_.contains("ERROR")).count()
    println(s"Nombre d'erreurs trouvées : $errorCount")

    // Suivi du comportement des utilisateurs
    // Cette étape extrait les chemins d'accès les plus fréquemment accédés à partir des logs.
    val mostAccessedPaths = logData.map(line => {
      val parts = line.split(" ")
      parts(6) // Chemin de la requête (supposé être à la 6ème position)
    }).groupBy($"value").count().sort($"count".desc).limit(10)

    // Visualisation des données
    // Cette étape convertit les résultats de l'analyse en HTML pour une visualisation facile.
    val htmlMostAccessedPaths = mostAccessedPaths.collect().map(row => s"<li>${row.getString(0)}: ${row.getLong(1)}</li>").mkString("\n")

    // Création du code HTML avec les résultats
    // Ici, le code HTML est généré avec les résultats de l'analyse.
    val htmlResult =
      s"""
        |<!DOCTYPE html>
        |<html>
        |<head>
        |<meta charset="UTF-8">
        |<title>Résultats de l'analyse de logs</title>
        |<style>
        |/* Ajoutez vos styles CSS ici */
        |body {
        |    font-family: Arial, sans-serif;
        |    background-color: #f2f2f2;
        |    margin: 0;
        |    padding: 0;
        |}
        |
        |.container {
        |    max-width: 800px;
        |    margin: 0 auto;
        |    padding: 20px;
        |    background-color: #fff;
        |    box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
        |}
        |
        |h1 {
        |    color: #333;
        |}
        |
        |h2 {
        |    color: #555;
        |}
        |
        |ul {
        |    list-style-type: none;
        |    padding: 0;
        |}
        |
        |li {
        |    margin-bottom: 10px;
        |}
        |
        |/* Ajoutez d'autres styles selon vos besoins */
        |</style>
        |</head>
        |<body>
        |<div class="container">
        |    <h1>Résultats de l'analyse de logs</h1>
        |    <h2>Chemins d'accès les plus fréquemment accédés :</h2>
        |    <ul>
        |    $htmlMostAccessedPaths
        |    </ul>
        |    <!-- Ajoutez l'élément canvas pour le graphe -->
        |    <canvas id="myChart" style="width:600px;height:400px;"></canvas>
        |</div>
        |
        |<!-- Inclure la bibliothèque Chart.js -->
        |<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
        |<script>
        |// Récupérez les données du fichier JavaScript généré par Spark
        |var pathsData = []; // Remplacez par vos données
        |
        |// Initialisez et dessinez le graphe avec Chart.js
        |var ctx = document.getElementById('myChart').getContext('2d');
        |var myChart = new Chart(ctx, {
        |    type: 'bar',
        |    data: {
        |        labels: pathsData.map(item => item[0]),
        |        datasets: [{
        |            label: 'Nombre d\'accès',
        |            data: pathsData.map(item => item[1]),
        |            backgroundColor: 'rgba(255, 99, 132, 0.2)',
        |            borderColor: 'rgba(255, 99, 132, 1)',
        |            borderWidth: 1
        |        }]
        |    },
        |    options: {
        |        scales: {
        |            yAxes: [{
        |                ticks: {
        |                    beginAtZero: true
        |                }
        |            }]
        |        }
        |    }
        |});
        |</script>
        |</body>
        |</html>
        |""".stripMargin

    // Écriture des résultats dans un fichier HTML
    // Ici, les résultats sont écrits dans un fichier HTML pour une consultation facile.
    val filePath = "C:\\spark-weblog\\scala-seed-project\\src\\main\\scala\\Logs\\results.html"
    val file = new File(filePath)
    val printWriter = new PrintWriter(file)
    printWriter.println(htmlResult)
    printWriter.close()

    // Arrêt de la session Spark
    spark.stop()
 }
}
