from PyQt6.QtWidgets import QApplication, QMainWindow, QPushButton, QFileDialog, QTextEdit, QVBoxLayout, QWidget, QLabel, QMessageBox
from PyQt6.QtCore import Qt
from PyQt6.QtGui import QPixmap, QIcon
import re

class App(QMainWindow):
    def __init__(self):
        super().__init__()

        self.input_file = None
        self.output_file = None
        self.source = None
        self.candidate = None
        self.output = None

        self.setWindowTitle('Regex tester')
        self.setWindowIcon(QIcon('fcefyn.png'))

        self.title_label = QLabel('<h1>Regex Parser para Trabajo Final Progamación Concurrente</h1>')
        self.subtitle_label = QLabel('<h3>Grupo: Los Discípulos de Herobrine</h3>')
        self.image_label = QLabel()
        self.image_label.setPixmap(QPixmap('image.png')) 
        self.input_button = QPushButton('Seleccionar archivo con transiciones', clicked=self.select_input_file)
        self.output_button = QPushButton('Seleccionar archivo de destino', clicked=self.select_output_file)
        self.execute_button = QPushButton('Ejecutar test de Regex', clicked=self.execute_test)
        self.output_text = QTextEdit()

        layout = QVBoxLayout()
        layout.addWidget(self.title_label,alignment=Qt.AlignmentFlag.AlignCenter)
        layout.addWidget(self.subtitle_label,alignment=Qt.AlignmentFlag.AlignCenter)
        layout.addWidget(self.image_label,alignment=Qt.AlignmentFlag.AlignCenter)
        layout.addWidget(self.input_button,alignment=Qt.AlignmentFlag.AlignCenter)
        layout.addWidget(self.output_button,alignment=Qt.AlignmentFlag.AlignCenter)
        layout.addWidget(self.execute_button,alignment=Qt.AlignmentFlag.AlignCenter)
        layout.addWidget(self.output_text,alignment=Qt.AlignmentFlag.AlignCenter)

        central_widget = QWidget()
        central_widget.setLayout(layout)
        self.setCentralWidget(central_widget)

    def select_input_file(self):
        self.input_file, _ = QFileDialog.getOpenFileName(self, 'Select Input File', '', 'Text Files (*.txt)')
        self.source = open(self.input_file, 'r')
        self.candidate = self.source.readlines()[0]
        self.source.close()

    def select_output_file(self):
        self.output_file, _ = QFileDialog.getSaveFileName(self, 'Select Output File', '', 'Text Files (*.txt)')
        self.output = open(self.output_file, 'w')
        self.output.write(self.candidate)
        self.output.close()  

    def execute_test(self):
        if self.input_file and self.output_file:
            while True:
                    f = open(self.output_file,'r')
                    transitions = f.readlines()[0]
                    f.close()
                    match = re.subn('((T0)(.*?)(T2)(.*?)((T4)(.*?)(T6)(.*?)((T9)(.*?)(T11)|(T8)(.*?)(T10))|(T5)(.*?)(T7)(.*?)((T9)(.*?)(T11)|(T8)(.*?)(T10)))(.*?)(T12)(.*?)(T13))|(T1)(.*?)(T3)(.*?)((T4)(.*?)(T6)(.*?)((T8)(.*?)(T10)|(T9)(.*?)(T11))|(T5)(.*?)(T7)(.*?)((T8)(.*?)(T10)|(T9)(.*?)(T11)))(.*?)(T12)(.*?)(T13)','\g<3>\g<5>\g<8>\g<10>\g<13>\g<16>\g<19>\g<21>\g<24>\g<27>\g<29>\g<31>\g<34>\g<36>\g<39>\g<41>\g<44>\g<47>\g<50>\g<52>\g<55>\g<58>\g<60>\g<62>',transitions)
                    print(match)
                    f = open(self.output_file,'w')
                    f.write(match[0])
                    f.close()
                    if match[1] == 0:
                        self.output_text.setText('FAIL: sobraron transiciones')
                        break
                    if match[0] == '':
                        self.output_text.setText('SUCCESS, Test OK')
                        break
            QMessageBox.information(self,'Test',self.output_text.toPlainText())


app = QApplication([])
window = App()
window.show()
app.exec()