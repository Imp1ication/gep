#include <iostream>
#include <string>
#include <unordered_map>

#include "Kexp_parser.h"

using namespace std;

int main() {
    GepParam param("gep_param.txt");
    KexpParser parser("chromosome.txt", param);

    param.setTerm('a', 1.0);
    param.setTerm('b', 2.0);
    param.setTerm('c', 3.0);
    param.setTerm('x', 4.0);


    cout << parser.calculate(param) << endl;

}
