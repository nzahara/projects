using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Text;
using System.Windows.Forms;

namespace SimpleSerial
{
    public partial class Form1 : Form
    {
        // Add this variable
        StringBuilder sBuffer = new StringBuilder();
        Int32 display_count = 0;
        int sec = 0;
        int ms = 0;
        double time_diff;
        double speed;
        bool rx_complete = true;
        int count = 0;

        public Form1()
        {
            InitializeComponent();
        }

        private void buttonStart_Click(object sender, EventArgs e)
        {
            serialPort1.PortName = "COM3";
            serialPort1.BaudRate = 115200;
            serialPort1.ReadBufferSize = 4;

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
            textBox1.Clear();
            textBox1.AppendText(Convert.ToString(speed));
            textBox2.Clear();
            textBox2.AppendText(Convert.ToString(count));
        }

        private void serialPort1_DataReceived(object sender, System.IO.Ports.SerialDataReceivedEventArgs e)
        {
            byte[] data = new byte[serialPort1.ReadBufferSize];
            int bytesRead = serialPort1.Read(data, 0, 4);
            int j = 0;
            Int32 temp = 0;
            for (int i = 0; i < data.Length; i++)
            {
                //if (data[i]!= 0)
                {
                    if (i == 0)
                    {
                        temp = Convert.ToInt32(data[i]) * 256;
                        //Console.WriteLine(temp);
                        //Console.WriteLine(rx_complete);
                    }
                    else if (i == 1)
                    {
                        temp = temp + Convert.ToInt32(data[i]);
                        //Console.WriteLine(temp);
                        sec = temp;
                        temp = 0;
                    }
                    else if (i == 2)
                    {
                        temp = Convert.ToInt32(data[i]) * 256;
                    }
                    else if (i == 3)
                    {
                        temp = temp + Convert.ToInt32(data[i]);
                        //Console.WriteLine(temp);
                        ms = temp;
                        temp = 0;
                        rx_complete = true;

                    }
                    if (rx_complete && i == data.Length - 1)
                    {
                        Console.WriteLine("test");
                        Console.WriteLine(sec);
                        Console.WriteLine(ms);
                        time_diff = sec + (ms / 1000.0);
                        Console.WriteLine(time_diff);
                        speed = (1.0 / time_diff) * (18.0 / 5);
                        Console.WriteLine(speed);
                        count++;
                        this.Invoke(new EventHandler(DisplayText));
                        ms = 0;
                        sec = 0;
                    }

                    //temp = Convert.ToInt32(data[i]);
                    //Console.WriteLine(temp);
                    //sBuffer.Append(Convert.ToInt32(data[i]).ToString("x"));

                    //Console.WriteLine(i);
                }
                sBuffer.ToString().ToUpper();

                //i = BitConverter.ToUInt32(data, 0);
            }
        }

        private void textBox2_TextChanged(object sender, EventArgs e)
        {

        }
    }
}