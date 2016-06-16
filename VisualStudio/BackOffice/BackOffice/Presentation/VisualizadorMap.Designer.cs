using BackOffice.Business;
using GMap.NET;
using GMap.NET.MapProviders;
using GMap.NET.WindowsForms;
using GMap.NET.WindowsForms.Markers;
using System.Collections.Generic;
using System.Device.Location;
using System.Drawing;
using System.Linq;

namespace BackOffice.Presentation
{
    partial class VisualizadorMap
    { 
       private System.ComponentModel.IContainer components = null;
        int index;

       protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Windows Form Designer generated code

        private void InitializeComponent()
        {
            System.ComponentModel.ComponentResourceManager resources = new System.ComponentModel.ComponentResourceManager(typeof(VisualizadorMap));
            this.gmap = new GMap.NET.WindowsForms.GMapControl();
            this.label1 = new System.Windows.Forms.Label();
            this.comboBox1 = new System.Windows.Forms.ComboBox();
            this.label2 = new System.Windows.Forms.Label();
            this.richTextBox1 = new System.Windows.Forms.RichTextBox();
            this.label3 = new System.Windows.Forms.Label();
            this.pictureBox1 = new System.Windows.Forms.PictureBox();
            this.button1 = new System.Windows.Forms.Button();
            this.button2 = new System.Windows.Forms.Button();
            this.label4 = new System.Windows.Forms.Label();
            this.button3 = new System.Windows.Forms.Button();
            this.richTextBox2 = new System.Windows.Forms.RichTextBox();
            ((System.ComponentModel.ISupportInitialize)(this.pictureBox1)).BeginInit();
            this.SuspendLayout();
            // 
            // gmap
            // 
            this.gmap.Bearing = 0F;
            this.gmap.CanDragMap = true;
            this.gmap.EmptyTileColor = System.Drawing.Color.Navy;
            this.gmap.GrayScaleMode = false;
            this.gmap.HelperLineOption = GMap.NET.WindowsForms.HelperLineOptions.DontShow;
            this.gmap.LevelsKeepInMemmory = 5;
            this.gmap.Location = new System.Drawing.Point(-2, -1);
            this.gmap.Margin = new System.Windows.Forms.Padding(5, 4, 5, 4);
            this.gmap.MarkersEnabled = true;
            this.gmap.MaxZoom = 100;
            this.gmap.MinZoom = 2;
            this.gmap.MouseWheelZoomType = GMap.NET.MouseWheelZoomType.MousePositionWithoutCenter;
            this.gmap.Name = "gmap";
            this.gmap.NegativeMode = false;
            this.gmap.PolygonsEnabled = true;
            this.gmap.RetryLoadTile = 0;
            this.gmap.RoutesEnabled = true;
            this.gmap.ScaleMode = GMap.NET.WindowsForms.ScaleModes.Integer;
            this.gmap.SelectedAreaFillColor = System.Drawing.Color.FromArgb(((int)(((byte)(33)))), ((int)(((byte)(65)))), ((int)(((byte)(105)))), ((int)(((byte)(225)))));
            this.gmap.ShowTileGridLines = false;
            this.gmap.Size = new System.Drawing.Size(1010, 790);
            this.gmap.TabIndex = 0;
            this.gmap.Zoom = 15D;
            // 
            // label1
            // 
            this.label1.AutoSize = true;
            this.label1.Font = new System.Drawing.Font("Arial", 14F, System.Drawing.FontStyle.Bold);
            this.label1.Location = new System.Drawing.Point(1016, 12);
            this.label1.Name = "label1";
            this.label1.Size = new System.Drawing.Size(148, 22);
            this.label1.TabIndex = 1;
            this.label1.Text = "Selecione Nota";
            // 
            // comboBox1
            // 
            this.comboBox1.DropDownStyle = System.Windows.Forms.ComboBoxStyle.DropDownList;
            this.comboBox1.Location = new System.Drawing.Point(1186, 12);
            this.comboBox1.Name = "comboBox1";
            this.comboBox1.Size = new System.Drawing.Size(232, 26);
            this.comboBox1.TabIndex = 2;
            this.comboBox1.SelectedIndexChanged += new System.EventHandler(this.comboBox1_SelectedIndexChanged);
            // 
            // label2
            // 
            this.label2.AutoSize = true;
            this.label2.Font = new System.Drawing.Font("Arial", 12F, System.Drawing.FontStyle.Bold);
            this.label2.Location = new System.Drawing.Point(1019, 52);
            this.label2.Name = "label2";
            this.label2.Size = new System.Drawing.Size(104, 19);
            this.label2.TabIndex = 3;
            this.label2.Text = "Nota Textual";
            // 
            // richTextBox1
            // 
            this.richTextBox1.BackColor = System.Drawing.SystemColors.Control;
            this.richTextBox1.BorderStyle = System.Windows.Forms.BorderStyle.FixedSingle;
            this.richTextBox1.Location = new System.Drawing.Point(1028, 98);
            this.richTextBox1.Name = "richTextBox1";
            this.richTextBox1.ReadOnly = true;
            this.richTextBox1.Size = new System.Drawing.Size(391, 152);
            this.richTextBox1.TabIndex = 4;
            this.richTextBox1.Text = "";
            // 
            // label3
            // 
            this.label3.AutoSize = true;
            this.label3.Font = new System.Drawing.Font("Arial", 12F, System.Drawing.FontStyle.Bold);
            this.label3.Location = new System.Drawing.Point(1019, 280);
            this.label3.Name = "label3";
            this.label3.Size = new System.Drawing.Size(74, 19);
            this.label3.TabIndex = 5;
            this.label3.Text = "Imagens";
            // 
            // pictureBox1
            // 
            this.pictureBox1.ErrorImage = null;
            this.pictureBox1.InitialImage = null;
            this.pictureBox1.Location = new System.Drawing.Point(1024, 324);
            this.pictureBox1.Name = "pictureBox1";
            this.pictureBox1.Size = new System.Drawing.Size(389, 251);
            this.pictureBox1.TabIndex = 6;
            this.pictureBox1.TabStop = false;
            // 
            // button1
            // 
            this.button1.Location = new System.Drawing.Point(1022, 581);
            this.button1.Name = "button1";
            this.button1.Size = new System.Drawing.Size(197, 30);
            this.button1.TabIndex = 7;
            this.button1.Text = "Anterior";
            this.button1.UseVisualStyleBackColor = true;
            this.button1.Click += new System.EventHandler(this.button1_Click);
            // 
            // button2
            // 
            this.button2.Location = new System.Drawing.Point(1225, 581);
            this.button2.Name = "button2";
            this.button2.Size = new System.Drawing.Size(188, 30);
            this.button2.TabIndex = 8;
            this.button2.Text = "Proxima";
            this.button2.UseVisualStyleBackColor = true;
            this.button2.Click += new System.EventHandler(this.button2_Click);
            // 
            // label4
            // 
            this.label4.AutoSize = true;
            this.label4.Font = new System.Drawing.Font("Arial", 12F, System.Drawing.FontStyle.Bold);
            this.label4.Location = new System.Drawing.Point(1020, 644);
            this.label4.Name = "label4";
            this.label4.Size = new System.Drawing.Size(91, 19);
            this.label4.TabIndex = 9;
            this.label4.Text = "Nota Vocal";
            // 
            // button3
            // 
            this.button3.Location = new System.Drawing.Point(1225, 634);
            this.button3.Name = "button3";
            this.button3.Size = new System.Drawing.Size(188, 38);
            this.button3.TabIndex = 10;
            this.button3.Text = "Reproduzir";
            this.button3.UseVisualStyleBackColor = true;
            this.button3.Click += new System.EventHandler(this.button3_Click);
            // 
            // richTextBox2
            // 
            this.richTextBox2.BackColor = System.Drawing.SystemColors.Control;
            this.richTextBox2.BorderStyle = System.Windows.Forms.BorderStyle.FixedSingle;
            this.richTextBox2.Location = new System.Drawing.Point(1022, 691);
            this.richTextBox2.Name = "richTextBox2";
            this.richTextBox2.ReadOnly = true;
            this.richTextBox2.Size = new System.Drawing.Size(391, 88);
            this.richTextBox2.TabIndex = 11;
            this.richTextBox2.Text = "";
            // 
            // VisualizadorMap
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(9F, 18F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(1439, 790);
            this.Controls.Add(this.richTextBox2);
            this.Controls.Add(this.button3);
            this.Controls.Add(this.label4);
            this.Controls.Add(this.button2);
            this.Controls.Add(this.button1);
            this.Controls.Add(this.pictureBox1);
            this.Controls.Add(this.label3);
            this.Controls.Add(this.richTextBox1);
            this.Controls.Add(this.label2);
            this.Controls.Add(this.comboBox1);
            this.Controls.Add(this.label1);
            this.Controls.Add(this.gmap);
            this.Font = new System.Drawing.Font("Arial", 12F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.FormBorderStyle = System.Windows.Forms.FormBorderStyle.FixedSingle;
            this.Icon = ((System.Drawing.Icon)(resources.GetObject("$this.Icon")));
            this.Margin = new System.Windows.Forms.Padding(5, 4, 5, 4);
            this.MaximizeBox = false;
            this.MinimizeBox = false;
            this.Name = "VisualizadorMap";
            this.StartPosition = System.Windows.Forms.FormStartPosition.CenterScreen;
            this.Text = "Visualizador Mapa";
            ((System.ComponentModel.ISupportInitialize)(this.pictureBox1)).EndInit();
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        public void carregaMapa(Atividade a)
        {
            gmap.DragButton = System.Windows.Forms.MouseButtons.Left;
            Mapa map = a.percurso;
            Dictionary<int, GeoCoordinate> cords = map.cords;

            List<int> keyList = new List<int>(cords.Keys);

            PointLatLng inicio = new PointLatLng(cords[keyList.Min()].Latitude, cords[keyList.Min()].Longitude);
            PointLatLng fim = new PointLatLng(cords[keyList.Max()].Latitude, cords[keyList.Max()].Longitude);


            List<PointLatLng> points = new List<PointLatLng>();
            foreach (GeoCoordinate g in cords.Values)
            {
                points.Add(new PointLatLng(g.Latitude, g.Longitude));
            }

            // Initialize map:
            gmap.MapProvider = GMap.NET.MapProviders.BingMapProvider.Instance;
            GMap.NET.GMaps.Instance.Mode = GMap.NET.AccessMode.ServerOnly;

            // config map
            gmap.MapProvider = GMapProviders.OpenStreetMap;
            gmap.Position = new PointLatLng(cords[keyList.Min()].Latitude, cords[keyList.Min()].Longitude);

            //MapRoute route = GMap.NET.MapProviders.GoogleMapProvider.Instance.GetRoute(inicio, fim, true, false, 12);

            GMapRoute r = new GMapRoute(points, map.nomeProva);

            GMapOverlay routesOverlay = new GMapOverlay("routes");
            routesOverlay.Routes.Add(r);
            gmap.Overlays.Add(routesOverlay);

            GMapOverlay markersOverlay = new GMapOverlay("markers");
            GMarkerGoogle inicioMarker = new GMarkerGoogle(inicio, GMarkerGoogleType.green_big_go);
            GMarkerGoogle fimMarker = new GMarkerGoogle(fim, GMarkerGoogleType.red_big_stop);
            gmap.Overlays.Add(markersOverlay);
            markersOverlay.Markers.Add(inicioMarker);
            markersOverlay.Markers.Add(fimMarker);

            r.Stroke.Width = 5;
            r.Stroke.Color = Color.SeaGreen;
        }

        public void addMarker(GeoCoordinate gc)
        {
            GMapOverlay markersOverlay = new GMapOverlay("markers");
            GMarkerGoogle marker = new GMarkerGoogle(new PointLatLng(gc.Latitude, gc.Longitude),GMarkerGoogleType.yellow);
            gmap.Overlays.Add(markersOverlay);
            markersOverlay.Markers.Add(marker);
            this.index=gmap.Overlays.IndexOf(markersOverlay);


        }

        public void removeMarker()
        {
            gmap.Overlays.RemoveAt(this.index);
        }

        #endregion

        private GMap.NET.WindowsForms.GMapControl gmap;
        private System.Windows.Forms.Label label1;
        private System.Windows.Forms.Label label2;
        private System.Windows.Forms.RichTextBox richTextBox1;
        private System.Windows.Forms.Label label3;
        private System.Windows.Forms.PictureBox pictureBox1;
        private System.Windows.Forms.Button button1;
        private System.Windows.Forms.Button button2;
        private System.Windows.Forms.ComboBox comboBox1;
        private System.Windows.Forms.Label label4;
        private System.Windows.Forms.Button button3;
        private System.Windows.Forms.RichTextBox richTextBox2;
    }
}