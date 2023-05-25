#ifndef __KEXP_PARSER_H__
#define __KEXP_PARSER_H__

#include <iostream>
#include <cmath>
#include <string>
#include <vector>
#include <queue>
#include <stack>
#include <unordered_map>
#include <fstream>
#include <sstream>

using std::cerr;
using std::endl;
using std::string;
using std::vector;
using std::stack;
using std::unordered_map;

class GepParam {
private:
    unordered_map<char, double> term_;
    unordered_map<char, int> func_;
    int func_max_param;
    int head_len, tail_len, gene_len;

public:
    /* Constructor */
    GepParam(string fileName){
        std::ifstream fin(fileName);
        if (!fin.is_open()) {
            cerr << "Error opening file: " << fileName << endl;
        }

        string line;
        // read term
        getline(fin, line);
        getline(fin, line);
        
        std::istringstream termLineStream(line);
        string termStr;
        while (getline(termLineStream, termStr, ',')) {
            term_.insert({termStr[0], 0.0});
        }

        // read function
        getline(fin, line);
        getline(fin, line);

        int funcNum = std::stoi(line);
        func_max_param = 0;
        while (funcNum--) {
            getline(fin, line);

            std::istringstream funcLineStream(line);
            string funcKey, funcVal;


            getline(funcLineStream, funcKey, ',');
            getline(funcLineStream, funcVal, ',');

            func_.insert({funcKey[0], std::stoi(funcVal)});
            func_max_param = std::max(func_max_param, std::stoi(funcVal));
        }

        // read head_len
        getline(fin, line);
        getline(fin, line);

        head_len = std::stoi(line);
        tail_len = head_len * (func_max_param - 1) + 1;
        gene_len = head_len + tail_len + tail_len;

        fin.close();
    };

    /* Getter and Setter */
    void setTerm(char key, double val) { term_[key] = val; }
    double getTerm(char key) { return term_[key]; }

    int getFuncParam(char key) { return func_[key]; }

    int getHeadLen() { return head_len; }
    int getTailLen() { return tail_len; }
    int getGeneLen() { return gene_len; }

    /* Public method */
    bool isFunc(char key) { return func_.find(key) != func_.end(); }
    bool isTerm(char key) { return term_.find(key) != term_.end(); }
};

class KexpParser {
private:
    string chromosome_;
    vector<double> dc_value;
    vector<int> dc_index;

public:
    KexpParser(string fileName, GepParam& param) {
        std::ifstream fin(fileName);
        if (!fin.is_open()) {
            cerr << "Error opening file: " << fileName << endl;
        }

        // read chromosome
        getline(fin, chromosome_);

        // parse dc_index
        string dcStr = chromosome_.substr(param.getHeadLen() + param.getTailLen(), param.getTailLen());
        for (char c : dcStr) {
            dc_index.push_back(c - '0');
        }

        // replace ? to dc_index
        chromosome_ = chromosome_.substr(0, param.getHeadLen() + param.getTailLen());

        int dc_j = 0;
        for (int i = 0; i < chromosome_.size(); i++) {
            if (chromosome_[i] == '?') {
                chromosome_[i] = dc_index[dc_j] + '0';
                dc_j = (dc_j + 1) % dc_index.size();
            }
        }

        // read dc_value
        string line;
        while (getline(fin, line)) {
            dc_value.push_back(std::stod(line));
        }
    }

    double calculate(GepParam& param) {
        // calculate expression lenth
        int term_need = param.getFuncParam(chromosome_[0]);
        int exp_len = 1;
        for (exp_len=1; exp_len<chromosome_.size(); ++exp_len) {
            if (param.isFunc(chromosome_[exp_len])) {
                term_need += param.getFuncParam(chromosome_[exp_len]);
            }

            term_need -= 1;

            if (term_need == 0) {
                break;
            }
        }

        // calculate expression
        stack<double> st;
        for (int i=exp_len; i >= 0; --i) {
            if ( param.isFunc(chromosome_[i]) ) {
                int paramNum = param.getFuncParam(chromosome_[i]);
                vector<double> vals;

                while (paramNum--) {
                    vals.push_back(st.top());
                    st.pop();
                }

                // switch case '+' '-' '*' '/' 'S'
                switch (chromosome_[i]) {
                    case '+':
                        st.push(vals[0] + vals[1]);
                        break;
                    case '-':
                        st.push(vals[0] - vals[1]);
                        break;
                    case '*':
                        st.push(vals[0] * vals[1]);
                        break;
                    case '/':
                        st.push(vals[0] / vals[1]);
                        break;
                    case 'S':
                        st.push(std::sqrt(vals[0]));
                        break;
                    default:
                        cerr << "Error: unknown function" << endl;
                        break;
                }
            } else if ( param.isTerm(chromosome_[i]) ) {
                st.push(param.getTerm(chromosome_[i]));
            } else {
                st.push(dc_value[chromosome_[i] - '0']);
            }
        }

        return st.top();
    }
};



#endif // __KEXP_PARSER_H__
