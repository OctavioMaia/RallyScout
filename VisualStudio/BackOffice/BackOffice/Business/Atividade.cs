using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using BackOffice;
using BackOffice.Business.Exceptions;
using itextsharp.pdfa;
using itextsharp;
using iTextSharp;
using iTextSharp.text;
using iTextSharp.awt;
using iTextSharp.testutils;
using iTextSharp.xmp;
using iTextSharp.xtra;
using iTextSharp.text.pdf;
using System.IO;

namespace BackOffice.Business
{
    public class Atividade : IComparable
    {
        public int idAtividade { get; set; }
        public DateTime inicioReconhecimento { get; set; }
        public DateTime fimReconhecimento { get; set; }
        public string nomeEquipa { get; set; }
        public Boolean inprogress { get; set; }
        public Boolean done { get; set; }
        ///
        public List<Nota> notas { get; set; }
        public Mapa percurso { get; set; }
        public List<Veiculo> veiculos { get; set; }
        public Equipa equipa { get; set; }
        public Batedor batedor { get; set; }

        public Atividade(int id) //so para a leitura a partir do json
        {
            this.idAtividade = id;
            this.inicioReconhecimento = new DateTime(); //nao deixa meter nulo
            this.fimReconhecimento = new DateTime(); //nao deixa meter nulo
            this.nomeEquipa = null;
            this.inprogress = false;
            this.done = false;
            this.notas = new List<Nota>();
            this.percurso = null;
            this.veiculos = null;
            this.equipa = null;
            this.batedor = null;
        }

        public Atividade(int id, string mailEquipa, string nomeProva
            , string mapPath, List<Veiculo> veicls, Equipa equip,
            Batedor bat)
        {
            this.idAtividade = id;
            this.inicioReconhecimento = DateTime.Now; //nao deixa meter nulo
            this.fimReconhecimento= DateTime.Now; //nao deixa meter nulo
            this.nomeEquipa = equip.nome;
            this.inprogress = false;
            this.done = false;
            this.notas = new List<Nota>(); //quando uma atividade é inserida nao tem notas
            this.percurso = new Mapa(nomeProva, id, mapPath);
            this.veiculos = veicls;
            if (this.veiculos == null)
            {
                this.veiculos = new List<Veiculo>();
            }
            this.equipa = equip;
            this.batedor = bat;


        }

        public Atividade( int idAtividade,  DateTime inicioReconhecimento ,
         DateTime fimReconhecimento , string nomeEquipa , Boolean inprogress ,
         Boolean done , List<Nota> notas , Mapa percurso ,List<Veiculo> veiculos ,
         Equipa equipa , Batedor batedor )
        {
            this.idAtividade = idAtividade;
            this.inicioReconhecimento = inicioReconhecimento;
            this.fimReconhecimento = fimReconhecimento;
            this.nomeEquipa = nomeEquipa;
            this.inprogress = inprogress;
            this.done = done;
            this.notas = notas;
            this.percurso = percurso;
            this.veiculos = veiculos;
            this.equipa = equipa;
            this.batedor = batedor;
        }

        public void addNota(Nota n)
        {
            if (this.notas == null)
            {
                this.notas = new List<Nota>();
                this.notas.Add(n);
            }
            else
            {
               if(this.notas.Contains(n))
                {
                    throw new NotaRepetidaException("Nota " + n.idNota + " Repetida");
                }
                this.notas.Add(n);
            }
        }

        public void addVeiculo(Veiculo v)
        {
            if (this.veiculos == null)
            {
                this.veiculos = new List<Veiculo>();
                this.veiculos.Add(v);
            }
            else
            {
                if (this.veiculos.Contains(v))
                {
                    throw new VeiculoRepetidoException("Veiculo " + v.chassi + " Já existe");
                }
                this.veiculos.Add(v);
            }
        }

        public void startReconhecimento()
        {
            if (this.inprogress == true)
            {
                throw new AtividadeJaIniciadaException("Atividade Já Foi Iniciada");
            }
            this.inprogress = true;
            this.inicioReconhecimento = DateTime.Now;
        }

        public void stopReconhecimento()
        {
            if (this.inprogress == false)
            {
                throw new AtividadeNaoIniciadaException("Atividade Não Foi Iniciada");
            }
            this.inprogress = false;
            this.done = true;
            this.fimReconhecimento = DateTime.Now;

            TimeSpan ts = this.fimReconhecimento - this.inicioReconhecimento;

            Double horas = Math.Round((ts.Hours * 60.0 + ts.Minutes) / 60.0, 3);
            this.batedor.updateHorasAndActiv(horas);
        }

        public void stopReconhecimento(Atividade toUpdateFrom)
        {
            if (this.inprogress == false)
            {
                throw new AtividadeNaoIniciadaException("Atividade Não Foi Iniciada");
            }
            this.notas = toUpdateFrom.notas;
            this.inprogress = false;
            this.done = true;
            this.fimReconhecimento = DateTime.Now;
        }

        public void addVeiculo(string mod, string marc, string chassie, List<string> carac)
        {
            Veiculo v = new Veiculo(mod, marc, chassie, carac);
            this.addVeiculo(v);
        }

        public void generateReportCopiloto(string path)
        {

            var NotaFont = FontFactory.GetFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.BLACK);
            var TitleFont = FontFactory.GetFont(FontFactory.HELVETICA_BOLD, 20, BaseColor.BLACK);
            var distFont = FontFactory.GetFont(FontFactory.HELVETICA, 12, BaseColor.BLACK);
            Document doc = new Document(iTextSharp.text.PageSize.LETTER, 10, 10, 42, 35);
            PdfWriter wri = PdfWriter.GetInstance(doc, new FileStream(path, FileMode.Create));
            doc.Open();
            Paragraph p = new Paragraph("Report from " + percurso.nomeProva + " to Team " + equipa.nome + ".\n\n\n\n", TitleFont);
            p.Alignment = Element.ALIGN_CENTER;
            doc.Add(p);

            PdfPTable t = new PdfPTable(2);
            foreach (Nota n in notas)
            {
                if (n.asVoice())
                {

                    String nota = n.getToPiloto();
                    double startDist = n.getDistanceToBegin(this.percurso);
                    double endDist = n.getDistanceToFinish(this.percurso);
                    String dist = startDist + "\n\n(" + endDist + ")";
                    var c1 = new PdfPCell(new Phrase(dist, distFont));
                    var c2 = new PdfPCell(new Phrase(nota, NotaFont));
                    c1.HorizontalAlignment = Element.ALIGN_CENTER;
                    c1.VerticalAlignment = Element.ALIGN_CENTER;
                    c2.HorizontalAlignment = Element.ALIGN_CENTER;
                    c2.VerticalAlignment = Element.ALIGN_CENTER;
                    t.AddCell(c1);
                    t.AddCell(c2);
                }
            }

            doc.Add(t);
            doc.Close();
        }

        public void generateReportGlobal(string path)
        {
            float parID = 15f;
            float marID = 15f;
            var NotaFont = FontFactory.GetFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.BLACK);
            var TitleFont = FontFactory.GetFont(FontFactory.HELVETICA_BOLD, 20, BaseColor.BLACK);
            var Huge = FontFactory.GetFont(FontFactory.HELVETICA_BOLD, 72, BaseColor.BLACK);
            var TextoFont = FontFactory.GetFont(FontFactory.HELVETICA, 18, BaseColor.BLACK); ;
            Document doc = new Document(iTextSharp.text.PageSize.LETTER, 10, 10, 42, 35);
            PdfWriter wri = PdfWriter.GetInstance(doc, new FileStream(path, FileMode.Create));
            doc.Open();

            Paragraph p = new Paragraph("Relatório Completo da Atividade número " + this.idAtividade + "\n\n", TitleFont);
            p.Alignment = Element.ALIGN_CENTER;
            doc.Add(p);

            ////
            string intro = "\tRelatório Completo da prova " + this.percurso.nomeProva +
                 " para a Equipa com o nome " + this.nomeEquipa + " (" + this.equipa.email + ").";
            p = new Paragraph(intro, TextoFont);
            /*p.FirstLineIndent = parID;
            p.IndentationLeft = marID;
            p.IndentationRight = marID;*/
            doc.Add(p);
            ///
            string infBta = "\tO reconhecimento foi efetuado pelo batedor " + this.batedor.nome +
                  " com o email de contacto " + this.batedor.email + ".";
            p = new Paragraph(infBta, TextoFont);
            /*p.FirstLineIndent = parID;
            p.IndentationLeft = marID;
            p.IndentationRight = marID;*/
            doc.Add(p);
            ///
            TimeSpan ts = this.fimReconhecimento - this.inicioReconhecimento;
            Double horas = Math.Round((ts.Hours * 60.0 + ts.Minutes) / 60.0,3);

            string infoRec = "\tA realização deste reconhecimento foi iniciada no dia " + this.inicioReconhecimento.ToShortDateString() +
                " às " + this.inicioReconhecimento.ToShortTimeString() + ",  sendo finalizado o seu reconhecimento no dia " + this.fimReconhecimento.ToShortDateString() +
                " às " + this.fimReconhecimento.ToShortTimeString() + ", tendo por isso uma duração de " + horas + " horas.";
            p = new Paragraph(infoRec, TextoFont);
            /*p.FirstLineIndent = parID;
            p.IndentationLeft = marID;
            p.IndentationRight = marID;*/
            doc.Add(p);
            ///Veiculos
            doc.NewPage();
            p = new Paragraph("Informação dos Veículos\n\n", TitleFont);
            p.Alignment = Element.ALIGN_CENTER;
            doc.Add(p);
            ///
            string veic = "\tPara a prova considerada foram os seguintes " + this.veiculos.Count + " veículos.";
            p = new Paragraph(infoRec, TextoFont);
            /*p.FirstLineIndent = parID;
            p.IndentationLeft = marID;
            p.IndentationRight = marID;*/
            doc.Add(p);
            //
            List listaVec = new List(List.ORDERED, 20f);
            listaVec.IndentationLeft = 20f;
            // listaVec.PreSymbol = string.Format("{0}.", i);
            foreach (Veiculo v in this.veiculos)
            {
                listaVec.Add("Chassi: " + v.chassi + " Marca: " + v.marca + " Modelo: " + v.modelo + "\n Características (" + v.caracteristicas.Count + ")");
                List listaVecC = new List(List.ORDERED, 30f);
                foreach (string c in v.caracteristicas)
                {
                    listaVecC.Add(c);
                }
                listaVec.Add(listaVecC);
            }
            doc.Add(listaVec);


            ///Notas
            doc.NewPage();
            p = new Paragraph("Informação das Notas\n\n", TitleFont);
            p.Alignment = Element.ALIGN_CENTER;
            doc.Add(p);
            ///
            string notas = "Para a prova considerada foram recolhidas " + this.notas.Count + " notas.";
            p = new Paragraph(notas, TextoFont);
            /*p.FirstLineIndent = parID;
            p.IndentationLeft = marID;
            p.IndentationRight = marID;*/
            doc.Add(p);
            //
            List listaNote = new List(List.ORDERED, 20f);
            listaNote.IndentationLeft = 20f;
            this.notas.Sort();
            foreach (Nota n in this.notas)
            {
                string nt = n.notaTextual;
                if (nt == null)
                {
                    nt = "";
                }
                string nv = "";
                Voz voz = n.notasVoz;
                if (voz != null)
                {
                    if (voz.texto != null)
                    {
                        nv = voz.texto;
                    }
                }
                string notas1 = ("Nota numero " + n.idNota + " \nrecolhida em " + n.localRegisto.Latitude + " " + n.localRegisto.Longitude + ".\n Nota Textual : " + nt + "\n Nota de Voz: " + nv+
                    "Imagens "  + n.imagens.Count + "\n");
                listaNote.Add(notas1);

                
            }
            doc.Add(listaNote);
            //pagina imagens
            doc.NewPage();
            p = new Paragraph("Anexo de imagens \n\n", TitleFont);
            p.Alignment = Element.ALIGN_CENTER;
            doc.Add(p);
            int notan = 1;
            foreach (Nota n in this.notas)
            {
                int inum = 1;
                foreach (System.Drawing.Image image in n.imagens)
                {
                    iTextSharp.text.Image pic = iTextSharp.text.Image.GetInstance(image, System.Drawing.Imaging.ImageFormat.Bmp); //é bmp

                    this.addPDFImage(pic, doc, inum, n.imagens.Count,notan);
                    inum++;
                    //listaNoteI.Add("OLa");

                }
            }
            // listaVec.Add(listaNoteI);

            //pagina final
            doc.NewPage();
            string fim = "FIM";
            for (int i = 0; i < 3; i++)
            {
                fim = "\n" + fim;
            }
            p = new Paragraph(fim, Huge);
            p.Alignment = Element.ALIGN_CENTER;
            doc.Add(p);
            doc.Close();
        }

        public int CompareTo(object obj)
        {
            if (obj == null) return 1;

            Atividade otherActividade = obj as Atividade;
            if (otherActividade != null)
                return this.idAtividade.CompareTo(otherActividade.idAtividade);
            else
                throw new ArgumentException("Object is not a Atividade");
        }

        public Boolean isPendent()
        {
            if(this.inprogress==true && this.done== false)
            {
                return true;
            }
            return false;
        }


        private void addPDFImage(iTextSharp.text.Image pic, Document document,int num,int tot,int not)
        {
            var caption = FontFactory.GetFont(FontFactory.HELVETICA_BOLD, 8, BaseColor.BLACK);
            if (pic.Height > pic.Width)
            {
                //Maximum height is 800 pixels.
                float percentage = 0.0f;
                percentage = 350 / pic.Height;
                pic.ScalePercent(percentage * 100);
            }
            else
            {
                //Maximum width is 600 pixels.
                float percentage = 0.0f;
                percentage = 270 / pic.Width;
                pic.ScalePercent(percentage * 100);
            }

            pic.Border = iTextSharp.text.Rectangle.BOX;
            pic.BorderColor = iTextSharp.text.BaseColor.BLACK;
            pic.BorderWidth = 3f;
            pic.Alignment = Element.ALIGN_CENTER;
            Paragraph p = new Paragraph("Imagem número " +  num + " de " + tot +" da nota " +not+"\n", caption);
            p.Alignment = Element.ALIGN_CENTER;
            document.Add(pic);
            document.Add(p);

            p = new Paragraph("\n\n");
            document.Add(p);

        }
    }




}
