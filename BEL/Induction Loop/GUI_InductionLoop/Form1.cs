using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Text;
using System.Windows.Forms;
using System.IO;
using System.IO.Ports;

namespace SimpleSerial
{
    public partial class Form1 : Form
    {
        // Add this variable
        StringBuilder sBuffer = new StringBuilder();
        int fw_count = 0;
        int tw_count = 0;
        bool tw_flag = false;
        bool fw_flag = false;
        string sPort;

        DateTime dt = DateTime.Now;
       
        public Form1()
        {
            InitializeComponent();
            var ports = SerialPort.GetPortNames();
            comboBox1.DataSource = ports;
        }

        private void buttonStart_Click(object sender, EventArgs e)
        {
            serialPort1.PortName = sPort;
            serialPort1.BaudRate = 115200;
            serialPort1.ReadBufferSize = 2;

            serialPort1.Open();
            if (serialPort1.IsOpen)
            {
                buttonStart.Enabled = false;
                buttonStop.Enabled = true;
                textBox1.ReadOnly = false;
            }
        }

        private void buttonStop_Click(object sender, EventArgs e)
        {
            if (serialPort1.IsOpen)
            {
                serialPort1.Close();
                buttonStart.Enabled = true;
                buttonStop.Enabled = false;
                textBox1.ReadOnly = true;
                textBox1.Clear();
                textBox2.Clear();
                textBox3.Clear();
                fw_count = 0;
                tw_count = 0;
            }

        }

        private void Form1_FormClosing(object sender, FormClosingEventArgs e)
        {
            if (serialPort1.IsOpen) serialPort1.Close();
        }

        private void textBox1_KeyPress(object sender, KeyPressEventArgs e)
        {
            // If the port is closed, don't try to send a character.
            if (!serialPort1.IsOpen) return;

            // If the port is Open, declare a char[] array with one element.
            char[] buff = new char[1];

            // Load element 0 with the key character.
            buff[0] = e.KeyChar;

            // Send the one character buffer.
            serialPort1.Write(buff, 0, 1);

            // Set the KeyPress event as handled so the character won't
            // display locally. If you want it to display, omit the next line.
            e.Handled = true;
        }

        private void DisplayText(object sender, EventArgs e)
        {
            if (tw_flag)
            {
                textBox1.Clear();
                textBox1.AppendText(Convert.ToString(tw_count));
                textBox3.AppendText("Two Wheeler detected");
                System.Threading.Thread.Sleep(2000);
                textBox3.Clear();
                tw_flag = false;
            }
            if (fw_flag)
            {
                textBox2.Clear();
                textBox2.AppendText(Convert.ToString(fw_count));
                textBox3.AppendText("Four Wheeler detected");
                System.Threading.Thread.Sleep(2000);
                textBox3.Clear();
                fw_flag = false;
            }
            
        }

        private void serialPort1_DataReceived(object sender, System.IO.Ports.SerialDataReceivedEventArgs e)
        {
            byte[] data = new byte[serialPort1.ReadBufferSize];
            int bytesRead = serialPort1.Read(data, 0, 2);
            if (data[0] == 116)
            {
                tw_count += 1;
                tw_flag = true;
                this.Invoke(new EventHandler(DisplayText));
            }
            else if (data[0] == 102)
            {
            fw_count += 1;
            fw_flag = true;
            this.Invoke(new EventHandler(DisplayText));
            }

            string sDate = dt.ToShortDateString();
            string path = @"C:\Users\Nikunj\Desktop\database.csv";
            if (!File.Exists(path))
            {
                // Create a file to write to. 
                using (StreamWriter sw = File.CreateText(path))
                {
                    sw.WriteLine("Date, Two-wheeler count, Four-wheeler count, Total count");
                }
                StringBuilder sb = new StringBuilder();
                sb.Append(sDate);
                sb.Append(", ");
                sb.Append(tw_count);
                sb.Append(", ");
                sb.Append(fw_count);
                sb.Append(", ");
                sb.Append(tw_count + fw_count);
                sb.AppendLine(", ");
                using (StreamWriter outfile = File.AppendText(path))
                {
                    outfile.Write(sb.ToString());
                }
            }
            else
            {
                string[] file = File.ReadAllLines(path);
                string lastLine = file[file.GetUpperBound(0)];
                string[] lineElements = lastLine.Split(new char[] {','});
                
                lineElements[1] = tw_count.ToString();
                lineElements[2] = fw_count.ToString();
                lineElements[3] = (tw_count+fw_count).ToString();
                if (lineElements[0].Equals(sDate))
                {
                    file[file.GetUpperBound(0)] = String.Join(", ", lineElements);
                    File.WriteAllLines(path, file);
                }
                else
                {
                    string[] newfile = new String[file.GetLength(0) + 1];
                    lineElements[0] = sDate;
                    file.CopyTo(newfile, 0);
                    newfile[newfile.GetUpperBound(0)] = String.Join(", ", lineElements);
                    File.WriteAllLines(path, newfile);
                }
            }
            
        }

        private void textBox2_TextChanged(object sender, EventArgs e)
        {

        }

        private void textBox3_TextChanged(object sender, EventArgs e)
        {

        }

        private void comboBox1_SelectedIndexChanged(object sender, EventArgs e)
        {
            sPort = comboBox1.SelectedItem.ToString();
            Console.WriteLine(sPort);
        }
    }
}
