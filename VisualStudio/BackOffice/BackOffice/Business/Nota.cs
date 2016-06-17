using System;
using System.Collections.Generic;
using System.Collections;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Device.Location;
using System.Drawing;

namespace BackOffice.Business
{
    public class Nota : IComparable
    {
        public int idNota { get; set; }
        public string notaTextual { get; set; }
        public GeoCoordinate localRegisto { get; set; }
        public List<Image> imagens { get; set; }
        public byte[] voice { get; set; }
        public Voz notasVoz { get; set; }

        public Nota(int id, string texto, double lat, double longt, List<Image> imgs, byte[] voz)
        {
            this.idNota = id;
            this.notaTextual = texto;
            this.localRegisto = new GeoCoordinate(lat, longt);
            this.imagens = imgs;
            this.voice = voz;
            this.notasVoz = new Voz(voz);
            if (this.voice != null)
            {
                //this.notasVoz.convertAudio();
            }else
            {
                this.notasVoz = null;
            }

        }

        public int CompareTo(object obj)
        {
            if (obj == null) return 1;

            Nota nota = obj as Nota;
            if (nota != null)
                return this.idNota.CompareTo(nota.idNota);
            else
                throw new ArgumentException("Object is not a Nota");
        }

        public Nota( int idNota , string notaTextual , GeoCoordinate localRegisto ,
         List<Image> imagens, byte[] voice, Voz notasVoz)
        {
            this.idNota = idNota;
            this.notaTextual = notaTextual;
            this.localRegisto = localRegisto;
            this.imagens = imagens;
            this.voice = voice;
            this.notasVoz = notasVoz;
            if (this.voice == null)
            {
                this.notasVoz = null;
            }
        }


        public bool asVoice()
        {
            return this.notasVoz != null;
        }

        public string getToPiloto()
        {
            return this.notasVoz.texto;
        }

        public override bool Equals(object obj)
        {
            if (obj == null)
            {
                return false;
            }
            Nota n = obj as Nota;
            if ((System.Object)n == null)
            {
                return false;
            }
            return this.idNota == n.idNota;
        }



        public double getDistanceToBegin(Mapa m) //retorna em KM
        {
            double dist = 0;
           foreach(int key in m.cords.Keys)
            {
                if (m.cords[key].Equals(this.localRegisto))
                {
                    if (key != 0)
                    {
                        dist += m.cords[key - 1].GetDistanceTo(m.cords[key]);
                    }
                    break;
                }
                if (key != 0)
                {
                    dist += m.cords[key - 1].GetDistanceTo(m.cords[key]);
                }
            }
            return Math.Round(dist/1000, 2);
        }

        public double getDistanceToFinish(Mapa m) //retorna em KM
        {
            double dist = 0;
            bool start = false;
            int tot = m.cords.Keys.Count;
            foreach (int key in m.cords.Keys)
            {
                if (m.cords[key].Equals(this.localRegisto))
                {
                    start = true;
                    if(key+1!= tot)
                    {
                        dist += m.cords[key].GetDistanceTo(m.cords[key+1]);
                    }
                }
                if (start == true)
                {
                    if (key + 1 != tot)
                    {
                        dist += m.cords[key].GetDistanceTo(m.cords[key + 1]);
                    }

                }
            }
            return Math.Round(dist/1000, 2);
        }
    }


}
