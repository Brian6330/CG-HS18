
#include "lsystem.h"
#include <stack>
#include <memory>
#include <iostream>

std::string LindenmayerSystemDeterministic::expandSymbol(unsigned char const& sym) {
	/*============================================================
		DONE 1.1
		For a given symbol in the sequence, what should it be replaced with after expansion?
	*/
    auto search = rules.find(sym);

    if (search != rules.end())
    {
        return search->second;
    }
    else
    {
        return { char(sym) };
    }

	//============================================================

	/*
	You may find useful:
		map.find: Iterator to an element with key equivalent to key. If no such element is found, past-the-end (see end()) iterator is returned.
		http://en.cppreference.com/w/cpp/container/unordered_map/find
	*/
}

std::string LindenmayerSystem::expandOnce(std::string const& symbol_sequence) {
	/*============================================================
		DONE 1.2
		Perform one iteration of grammar expansion on `symbol_sequence`.
		Use the expandSymbol method
	*/
	std::string result = "";

	for (char character : symbol_sequence) {
		result += expandSymbol(character);
	}
	return result;

	//============================================================
}

std::string LindenmayerSystem::expand(std::string const& initial, uint32_t num_iters) {
	/*============================================================
		DONE 1.3
		Perform `num_iters` iterations of grammar expansion (use expandOnce)
	*/
    std::string sequence = initial;
	for(uint32_t l = 0; l < num_iters; ++l){
	    sequence = expandOnce(sequence);

	}
    return sequence;

	//============================================================
}

std::vector<Segment> LindenmayerSystem::draw(std::string const& symbols) {
	std::vector<Segment> lines; // this is already initialized as empty vector

	/*============================================================
		DONE
		Build line segments according to the sequence of symbols
		The initial position is (0, 0) and the initial direction is "up" (0, 1)
		Segment is std::pair<vec2, vec2>

		you can create a Segment as {p1, p2}, where p1 and p2 are vec2, e.g.:
		lines.push_back({p1, p2});

		You may also find std::stack useful:

		https://en.cppreference.com/w/cpp/container/stack

		There also is a mat2 class in utils/vec.* you may find useful for
		implementing rotations.
	*/

	vec2 pos = vec2(0,0);
    vec2 dir = vec2(0,1);
    vec2 destination = vec2(0.0);

    Segment top;
    std::stack<Segment> stack;

    mat2 rotation_matrix = mat2(0);
    double angle = deg2rad(rotation_angle_deg);

    for (char chara : symbols)

        switch (chara) {

        case '+': {
            rotation_matrix =  mat2(cosf(angle), -sinf(angle), sinf(angle), cosf(angle));
            dir = normalize(rotation_matrix * dir);

            break;
        }

        case '-': {

            rotation_matrix =  mat2(cosf(-angle), -sinf(-angle), sinf(-angle), cosf(-angle));
            dir = normalize(rotation_matrix * dir);

            break;
        }

        case '[': {
            stack.push({pos, dir});
            break;
        }

        case ']': {

            top = stack.top();
            stack.pop();
            pos = top.first;
            dir = top.second;

            break;
        }

        default:{
            destination = pos + dir;
            lines.push_back({pos, destination });
            pos = destination;
        }

    }


	return lines;
	//============================================================
}

std::string LindenmayerSystemStochastic::expandSymbol(unsigned char const& sym) {
	/*============================================================
		DONE 4
		For a given symbol in the sequence, what should it be replaced with after expansion?
		(stochastic case)

		Use dice.roll() to get a random number between 0 and 1
	*/
	auto search = rules.find(sym);


	if (search != rules.end())
	{
		double r = dice.roll();
		double p = 0;
		const std::vector<StochasticRule> rule_list = search->second;

		for (int i = 0; i < rule_list.size(); i++) {
			if (r > p && r <= rule_list[i].probability + p) return rule_list[i].expansion;
			p += rule_list[i].probability;
		}
	}
	else
	{
		return { char(sym) };
	};

	//============================================================
}

void LindenmayerSystemDeterministic::addRuleDeterministic(unsigned char sym, std::string const& expansion) {
	rules[sym] = expansion;
}

void LindenmayerSystemStochastic::addRuleStochastic(unsigned char sym, std::vector<StochasticRule> expansions_with_ps) {
	rules[sym] = expansions_with_ps;
}
