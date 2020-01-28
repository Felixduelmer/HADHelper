Anleitung zur Erstellung einer Notification mit Android Studio und Silab

Android:

1. Download Android Studio 
2. Oeffne die Applikation HADHelper in Android Studio (kann geforked und gecloned werden von folgender Seite: https://gitlab.lrz.de/ga65fah/hadhelper.git)
3. Installiere Plugin Android Drawable Importer
	(Settings > Plugin > Browse Repository > Search Android Drawable Importer)
4. Um ein neues Icon fuer die Notification hinzuzufuegen:
	-Rechtsklick irgendwo im Strukturbaum und dann: new->Batch Drawable Importer
	-Source file aussuchen
	-Alle target resolutions auswaehlen
	-Target-Root angeben: \AndroidStudioProjects\HADHelper\app\src\main\res

	Nun sollte im drawable oder mipmap ordner ein Unterordner mit dem Bild in verschiedenen Aufloesungen vorhanden sein

	Fuer naehere Informationen: https://github.com/winterDroid/android-drawable-importer-intellij-plugin

5. Oeffne ListenerService (\AndroidStudioProjects\HADHelper\app\src\main\java\hadh\lfe\mw\tum\de\hadhelper\ListenerService.java)

6. Definiere in der Klasse ServiceHandler deinen eigenen Case
7. Veraendere die Push Notification Methode um dein Icon darzustellen (Eingefuegt bei Punkt 4) - entweder wird hier R.drawable oder R.mipmap. eingefuegt und dann der selbst definierte Name des Icons 
8. Die Nummer des definierten Case ist die Nummer, welche von Silab gesendet werden muss (in Ascii)

Silab

1. Integriere folgenden Code in deine DPU:
	
				DPUSCNXHedgehogKiller setcase {

				     Computer = {SCN};
				     Index = 99;
				   
				     Family = "Situation";
				     Name1 = "Situationsauswahl";

				     Default1 = 0.0;
				     #OutMode1 = (MonoFlop, 200);

				};

				 

				DPUSocket AndroidSocket {

				     Computer = {OP};
				     Index = 100;

				     Socket_IsTCPIP = false;
				     Socket_IsServer = false;
					 Socket_IP = "192.168.10.23";
					 Socket_PortSend = 58453;
				     SendDefinition =

				     (
				           (situation, byte, 0)
				     );

				};

				Connections = {

				    setcase.Out1 -> AndroidSocket.situation
				};

2. Veraendere die IP-Adresse je nach dem, was auf dem Android Geraet auf dem Hauptbildschirm der HADHelper Applikation angezeigt wird
3. Definiere deine Hedgehogs in der Modulbeschreibung (Achtung: Hier muessen Ascii-Werte eingegeben werden, d.h. wenn im Android Code der Case '1' definiert wird muss in Silab der Wert 49 losgeschickt werden)
