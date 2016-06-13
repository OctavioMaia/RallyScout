﻿using System;
using System.Collections.Generic;
using System.Device.Location;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using BackOffice.Business;

namespace BackOffice.Data.Json
{
    class BackToJust
    {
        public int idAtividade { get; set; }
        public Map mapa { get; set; }

        public BackToJust(Atividade a)
        {
            this.idAtividade = a.idAtividade;
            this.mapa = new Map();
            this.mapa.nomeProva = a.percurso.nomeProva;
            this.mapa.nomeEquipa = a.nomeEquipa;
            int totcar = a.veiculos.Count;
            Car[] carros = new Car[totcar];
            for(int i = 0; i < totcar; i++)
            {
                Veiculo v = a.veiculos[i];
                string[] caract = new string[v.caracteristicas.Count];
                for(int j=0;j< v.caracteristicas.Count; j++)
                {
                    caract[j] = v.caracteristicas[j];
                }
                Car c = new Car(v.chassi, caract);
                carros[i] = c;
            }
            Cord[] cords = new Cord[a.percurso.cords.Count];
            for(int i=0;i< a.percurso.cords.Count; i++)
            {
                GeoCoordinate g = a.percurso.cords[i];
                Cord c = new Cord(g.Latitude, g.Longitude);
                cords[i] = c;
            }
            this.mapa.percurso = cords;
            this.mapa.veiculos = carros;

        }

    }

    class Map
    {
        public String nomeProva { get; set; }
        public String nomeEquipa { get; set; }
        public Cord[] percurso { get; set; }
        public Car[] veiculos { get; set; }
    }
    class Cord
    {
        public double lat { get; set; }
        public double log { get; set; }
        public Cord(double lati,double longi)
        {
            this.lat = lati;
            this.log = longi;
        }
    }
    class Car
    {
        public string chassi { get; set; }
        public string[] caracteristicas { get; set; }
        public Car(string chass, string[] caract)
        {
            this.chassi = chass;
            this.caracteristicas = caract;
        }
    }
}