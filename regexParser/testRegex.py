import re

source = open('transitions.txt','r')
candidate = source.readlines()[0]
source.close()

output = open('output.txt','w')
output.write(candidate)
output.close()

while True:
    f = open('output.txt','r')
    transitions = f.readlines()[0]
    f.close()
    match = re.subn('((T0)(.*?)(T2)(.*?)((T4)(.*?)(T6)(.*?)((T9)(.*?)(T11)|(T8)(.*?)(T10))|(T5)(.*?)(T7)(.*?)((T9)(.*?)(T11)|(T8)(.*?)(T10)))(.*?)(T12)(.*?)(T13))|(T1)(.*?)(T3)(.*?)((T4)(.*?)(T6)(.*?)((T8)(.*?)(T10)|(T9)(.*?)(T11))|(T5)(.*?)(T7)(.*?)((T8)(.*?)(T10)|(T9)(.*?)(T11)))(.*?)(T12)(.*?)(T13)','\g<3>\g<5>\g<8>\g<10>\g<13>\g<16>\g<19>\g<21>\g<24>\g<27>\g<29>\g<31>\g<34>\g<36>\g<39>\g<41>\g<44>\g<47>\g<50>\g<52>\g<55>\g<58>\g<60>\g<62>',transitions)
    print(match)
    f = open('output.txt','w')
    f.write(match[0])
    f.close()
    if match[1] == 0:
        print('FAIL: sobraron transiciones')
        break
    if match[0] == '':
        print('SUCCESS, Test OK')
        break