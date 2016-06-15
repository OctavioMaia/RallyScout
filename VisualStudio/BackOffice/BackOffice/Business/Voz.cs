using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Speech.AudioFormat;
using System.Speech.Recognition;
using System.Text;
using System.Threading.Tasks;

namespace BackOffice.Business
{
    public class Voz
    {
        public byte[] audio { get; set; }
        public string texto { get; set; }

        public Voz()
        {
            this.audio = null;
            this.texto = null;
        }


        public Voz(byte[] audio, string texto)
        {
            this.audio = audio;
            this.texto = texto;
        }

        public Voz(byte[] voice)
        {
            this.audio = voice;
            this.texto = null;
        }


        public void convertAudio()
        {
            string path  = Path.GetTempPath();
            path = path + string.Format("HH_mm_ss", DateTime.Now)+".wav";
            //cria o ficheiro temporario com audio
            File.WriteAllBytes(path, audio);
            SpeechRecognitionEngine recognizer = new SpeechRecognitionEngine();
            recognizer.LoadGrammar(BackOfficeAPP.gramatica);

            // Configure the input to the recognizer.
            recognizer.SetInputToAudioStream(
              File.OpenRead(path),
              new SpeechAudioFormatInfo(
                44100, AudioBitsPerSample.Sixteen, AudioChannel.Mono));

            recognizer.SpeechRecognized +=
              new EventHandler<SpeechRecognizedEventArgs>(sre_SpeechRecognized);

            // Start recognition.
            recognizer.Recognize();

            //elimina o ficheiro temporario com audio
            File.Delete(path);


        }

        // Create a simple handler for the SpeechRecognized event.
        void sre_SpeechRecognized(object sender, SpeechRecognizedEventArgs e)
        {
            this.texto= e.Result.Text;
        }

    }
}
