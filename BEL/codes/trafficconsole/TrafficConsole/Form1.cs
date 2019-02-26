using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using System.IO.Ports;

namespace TrafficConsole
{
    public partial class Form1 : Form
    {
        int i = 0;
        public Form1()
        {
            InitializeComponent();
        }

        void serialPort1_DataReceived(object sender, SerialDataReceivedEventArgs e)
        {
            SerialPort port = (SerialPort)sender;

            byte[] buffer = new byte[port.BytesToRead];
            port.Read(buffer, 0, buffer.Length);
            string data = UnicodeEncoding.ASCII.GetString(buffer);
            //if (label1.InvokeRequired)
            //{
            //    Invoke(new EventHandler(DisplayData), data, EventArgs.Empty);
            //}
            //else
            //{
                
            //}
        }

        private void DisplayData(object sender, EventArgs e)
        {
            string data = (string)sender;
            count.Text = "blah";
        }

        private void pictureBox1_Click(object sender, EventArgs e)
        {

        }

        private void Form1_Load(object sender, EventArgs e)
        {
        }

        private void count_Click(object sender, EventArgs e)
        {
            
        }

        private void backgroundWorker1_DoWork(object sender, DoWorkEventArgs e)
        {
            i++;
        }

        private void label1_Click(object sender, EventArgs e)
        {
            
        }

       

    }
}
