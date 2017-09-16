/*
 * main.c
 *
 *  Created on: Oct 8, 2013
 *      Author: lim
 */
#include <avr/io.h>
#include <avr/interrupt.h>
#include <avr/pgmspace.h>
#include "ports.h"

const unsigned int PROGMEM packedDeltaTimes[] = {0, 16, 32, 48, 64, 96, 144, 192};
#define PACKED_DELTA_TIMES_LENGTH 8
const unsigned int PROGMEM packedDurations[] = {46, 48, 62, 94, 160, 528, 720};
#define PACKED_DURATIONS_LENGTH 7
const unsigned int PROGMEM packedFrequencies[] = {165, 175, 220, 233, 247, 262, 277, 294, 311, 330, 349, 370, 392, 415, 440, 466, 494, 554, 587, 622, 659, 698, 740, 784, 831, 880, 988, 1047, 1109, 1175, 1245, 1319, 1480, 1760};
#define PACKED_FREQUENCIES_LENGTH 34
const unsigned int PROGMEM packedNotes[] = {19, 28, 4, 211, 28, 4, 339, 28, 4, 339, 25, 4, 211, 28, 4, 856, 543, 532, 2516, 521, 465, 25, 9, 398, 20, 6, 393, 17, 2, 398, 22, 7, 336, 24, 9, 335, 23, 8, 206, 22, 7, 1358, 1044, 1030, 1300, 1052, 1038, 1304, 1055, 1041, 281, 32, 18, 342, 29, 16, 1752, 1567, 1553, 342, 28, 14, 337, 25, 11, 210, 26, 13, 208, 24, 9, 401, 25, 9, 398, 20, 6, 393, 17, 2, 398, 22, 7, 336, 24, 9, 335, 23, 8, 206, 22, 7, 1358, 1044, 1030, 1300, 1052, 1038, 1304, 1055, 1041, 281, 32, 18, 342, 29, 16, 1752, 1567, 1553, 342, 28, 14, 337, 25, 11, 210, 26, 13, 208, 24, 9, 386, 348, 31, 219, 30, 9, 218, 29, 216, 27, 206, 217, 28, 199, 209, 21, 210, 22, 212, 25, 14, 334, 22, 14, 209, 25, 7, 210, 26, 194, 348, 31, 219, 30, 6, 218, 29, 216, 27, 201, 217, 28, 14, 349, 31, 33, 349, 31, 33, 221, 31, 33, 329, 322, 348, 31, 219, 30, 9, 218, 29, 216, 27, 206, 217, 28, 199, 209, 21, 210, 22, 212, 25, 14, 334, 22, 14, 209, 25, 7, 210, 26, 194, 341, 27, 10, 402, 26, 12, 401, 25, 14, 393, 201, 322, 322, 348, 31, 219, 30, 9, 218, 29, 216, 27, 206, 217, 28, 199, 209, 21, 210, 22, 212, 25, 14, 334, 22, 14, 209, 25, 7, 210, 26, 194, 348, 31, 219, 30, 6, 218, 29, 216, 27, 201, 217, 28, 14, 349, 31, 33, 349, 31, 33, 221, 31, 33, 329, 322, 348, 31, 219, 30, 9, 218, 29, 216, 27, 206, 217, 28, 199, 209, 21, 210, 22, 212, 25, 14, 334, 22, 14, 209, 25, 7, 210, 26, 194, 341, 27, 10, 402, 26, 12, 401, 25, 14, 393, 201, 322, 341, 25, 1, 213, 25, 341, 25, 5, 341, 25, 215, 26, 10, 340, 28, 9, 209, 25, 337, 22, 2, 206, 20, 320, 341, 25, 1, 213, 25, 341, 25, 5, 341, 25, 215, 26, 10, 1748, 1564, 201, 386, 384, 341, 25, 1, 213, 25, 341, 25, 5, 341, 25, 215, 26, 10, 340, 28, 9, 209, 25, 337, 22, 2, 206, 20, 320, 339, 28, 4, 211, 28, 4, 339, 28, 4, 339, 25, 4, 211, 28, 4, 856, 543, 532, 2516, 521, 465, 25, 9, 398, 20, 6, 393, 17, 2, 398, 22, 7, 336, 24, 9, 335, 23, 8, 206, 22, 7, 1358, 1044, 1030, 1300, 1052, 1038, 1304, 1055, 1041, 281, 32, 18, 342, 29, 16, 1752, 1567, 1553, 342, 28, 14, 337, 25, 11, 210, 26, 13, 208, 24, 9, 401, 25, 9, 398, 20, 6, 393, 17, 2, 398, 22, 7, 336, 24, 9, 335, 23, 8, 206, 22, 7, 1358, 1044, 1030, 1300, 1052, 1038, 1304, 1055, 1041, 281, 32, 18, 342, 29, 16, 1752, 1567, 1553, 342, 28, 14, 337, 25, 11, 210, 26, 13, 208, 24, 9, 409, 28, 2, 214, 25, 337, 20, 8, 201, 337, 21, 14, 338, 22, 7, 217, 29, 199, 217, 29, 210, 22, 14, 206, 199, 1364, 1048, 4, 1309, 1056, 1309, 1056, 71, 1245, 1056, 9, 1308, 1055, 141, 1178, 1053, 281, 28, 9, 214, 25, 201, 210, 22, 209, 20, 14, 206, 201, 345, 28, 2, 214, 25, 337, 20, 8, 201, 337, 21, 14, 338, 22, 7, 217, 29, 199, 217, 29, 210, 22, 14, 206, 199, 340, 24, 9, 218, 29, 346, 29, 9, 1242, 1053, 1033, 1305, 1052, 1035, 1304, 1050, 1037, 276, 25, 14, 209, 201, 209, 206, 2, 473, 28, 2, 214, 25, 337, 20, 8, 201, 337, 21, 14, 338, 22, 7, 217, 29, 199, 217, 29, 210, 22, 14, 206, 199, 1364, 1048, 4, 1309, 1056, 1309, 1056, 71, 1245, 1056, 9, 1308, 1055, 141, 1178, 1053, 281, 28, 9, 214, 25, 201, 210, 22, 209, 20, 14, 206, 201, 345, 28, 2, 214, 25, 337, 20, 8, 201, 337, 21, 14, 338, 22, 7, 217, 29, 199, 217, 29, 210, 22, 14, 206, 199, 340, 24, 9, 218, 29, 346, 29, 9, 1242, 1053, 1033, 1305, 1052, 1035, 1304, 1050, 1037, 276, 25, 14, 209, 201, 209, 206, 2, 467, 28, 4, 211, 28, 4, 339, 28, 4, 339, 25, 4, 211, 28, 4, 856, 543, 532, 2516, 521, 473, 28, 2, 214, 25, 337, 20, 8, 201, 337, 21, 14, 338, 22, 7, 217, 29, 199, 217, 29, 210, 22, 14, 206, 199, 1364, 1048, 4, 1309, 1056, 1309, 1056, 71, 1245, 1056, 9, 1308, 1055, 141, 1178, 1053, 281, 28, 9, 214, 25, 201, 210, 22, 209, 20, 14, 206, 201, 345, 28, 2, 214, 25, 337, 20, 8, 201, 337, 21, 14, 338, 22, 7, 217, 29, 199, 217, 29, 210, 22, 14, 206, 199, 340, 24, 9, 218, 29, 346, 29, 9, 1242, 1053, 1033, 1305, 1052, 1035, 1304, 1050, 1037, 276, 25, 14, 209, 201, 209, 206, 2, 465, 25, 9, 398, 20, 6, 397, 17, 2, 1366, 2066, 2055, 1304, 1302, 1301, 2066, 2051, 1303, 1301, 273, 3092, 3074, 208, 2769};
#define PACKED_NOTES_LENGTH 856

#define PACKED_NOTES_BITS 6
#define PACKED_DELTATIMES_BITS 3
#define PACKED_DURATIONS_BITS 3

#define PACKED_DELTA_TIMES(index) pgm_read_word(&packedDeltaTimes[index])
#define PACKED_DURATIONS(index) pgm_read_word(&packedDurations[index])
#define PACKED_FREQUENCIES(index) pgm_read_word(&packedFrequencies[index])
#define PACKED_NOTES(index) pgm_read_word(&packedNotes[index])

#define SAMPLE_RATE_POW 15 //2^15=32768 samples per second
#define SAMPLE_RATE (1<<SAMPLE_RATE_POW)
#define SAMPLE_RATE_DIV2 (1<<(SAMPLE_RATE_POW-1))
#define SAMPLE_RATE_MASK (SAMPLE_RATE - 1)
#define OSC2_PRESCALLER 8
#define OSC2_OCRVALUE ((F_CPU>>SAMPLE_RATE_POW)/OSC2_PRESCALLER)

#define NOTE_QUATER 46
#define TEMPO_QUATERS_PER_MINUTE 400
//#define TEMPO_SAMPLES (60*SAMPLE_RATE/(TEMPO_QUATERS_PER_MINUTE*NOTE_QUATER))
#define TEMPO_SAMPLES 107

#define SIMULTANEOUS_NOTES 8


#define NOTE_BITOFFSET 0
#define NOTE_BITMASK (((1 << PACKED_NOTES_BITS) - 1) << NOTE_BITOFFSET)
#define DELTATIME_BITOFFSET (NOTE_BITOFFSET + PACKED_NOTES_BITS)
#define DELTATIME_BITMASK (((1 << PACKED_DELTATIMES_BITS) - 1) << DELTATIME_BITOFFSET)
#define DURATION_BITOFFSET (DELTATIME_BITOFFSET + PACKED_DELTATIMES_BITS)
#define DURATION_BITMASK (((1 << PACKED_DURATIONS_BITS) - 1) << DURATION_BITOFFSET)

#if defined(__AVR_ATmega328P__) || defined(__AVR_ATmega168P__) || defined(__AVR_ATmega168__)
#define LED_PORT PORT_B5
#elif defined(__AVR_ATmega16__) || defined(__AVR_ATmega32__)
#define LED_PORT PORT_B0
#endif

long t = 0;
unsigned int playingNotesFreq[SIMULTANEOUS_NOTES];
unsigned int playingNotesPhase[SIMULTANEOUS_NOTES];
int startIdx = 0;
int startTime = 0;
int startDuration = 0;
int endIdx = 0;
int endTime = 0;
int currentNotes = 0;
char refreshNotesList = 1;
int corTime = 0;
int corTimeCnt = 0;

#if defined(__AVR_ATmega328P__) || defined(__AVR_ATmega168P__) || defined(__AVR_ATmega168__)
ISR(TIMER2_COMPA_vect) //play next sample
{
	cli();
	t++;
	corTimeCnt++;
	if (corTimeCnt>=TEMPO_SAMPLES) {
		corTimeCnt = 0;
		corTime++;
		refreshNotesList = 1;
	}
	unsigned char buf = 0;

    for (int l = 0; l < currentNotes; l++) {
    	unsigned int keyFreq = playingNotesFreq[l];
		unsigned int phase = playingNotesPhase[l];
		phase += keyFreq;
		playingNotesPhase[l] = phase;
		unsigned int ab = phase & SAMPLE_RATE_MASK;
        buf += (unsigned char) ((ab<(SAMPLE_RATE_DIV2)?0:30));
	}

    OCR0A = buf; //here will be function
    sei();
}

void initPWM()
{
	SETD(PORT_D6); //set OC0A to output
	TCCR0A = (2<<COM0A0)|(1<<WGM00)|(1<<WGM01); //fast PWM, clear OC0 on compare match
	TCCR0B = (0b001<<CS00); //no prescaller
	OCR0A = 127;

	TCCR2A = (1<<WGM21)|(0<<WGM20); //CTC mode
	TCCR2B = (0b010<<CS20); //prescaller CLK/8
 	OCR2A = OSC2_OCRVALUE; // 16 000 000 / 32 768 / 8 = 61
 	TIMSK2 = (1<<OCIE2A); //Interrupt on second timer compare
}
#elif defined(__AVR_ATmega16__) || defined(__AVR_ATmega32__)
ISR(TIMER2_COMP_vect) //play next sample
{
	cli();
	t++;
	corTimeCnt++;
	if (corTimeCnt>=TEMPO_SAMPLES) {
		corTimeCnt = 0;
		corTime++;
		refreshNotesList = 1;
	}
	unsigned char buf = 0;

    for (int l = 0; l < currentNotes; l++) {
    	unsigned int keyFreq = playingNotesFreq[l];
		unsigned int phase = playingNotesPhase[l];
		phase += keyFreq;
		playingNotesPhase[l] = phase;
		unsigned int ab = phase & SAMPLE_RATE_MASK;
        buf += (unsigned char) ((ab<(SAMPLE_RATE_DIV2)?0:30));
	}

    OCR0 = buf; //here will be function
    sei();
}

void initPWM()
{
	SETD(PORT_B3); //set OC0 to output
	TCCR0 = (2<<COM00)|(1<<WGM00)|(1<<WGM01)|(0b001<<CS00); //fast PWM, clear OC0 on compare match, no prescaller
	OCR0 = 127;

	TCCR2 = (1<<WGM21)|(0<<WGM20)|(0b010<<CS20); //CTC mode, prescaller CLK/8
 	OCR2 = OSC2_OCRVALUE; // 16 000 000 / 32 768 / 8 = 61
 	TIMSK = (1<<OCIE2); //Interrupt on second timer compare
}
#endif


void init()
{
	initPWM();
	SETD(LED_PORT);
	sei();
}

void main()
{
	init();
	startDuration = PACKED_DURATIONS((PACKED_NOTES(startIdx) & DURATION_BITMASK) >> DURATION_BITOFFSET);
	SETP(LED_PORT);

	int ii = 1;
	while (ii) {
//		if (t & 0x0400) {
//			SETP(PORT_B0);
//		} else {
//			CLRP(PORT_B0);
//		}
		ii++;
		ii = ii?1:0;

		if (refreshNotesList) {
			refreshNotesList = 0;

        	while ((startIdx+1 < PACKED_NOTES_LENGTH) && (startTime + startDuration <= corTime)) {
    			INVP(LED_PORT);
        		startIdx++;
        		int packedNote = PACKED_NOTES(startIdx);
				int deltaTime = PACKED_DELTA_TIMES((packedNote & DELTATIME_BITMASK) >> DELTATIME_BITOFFSET);
			    startDuration = PACKED_DURATIONS((packedNote & DURATION_BITMASK) >> DURATION_BITOFFSET);
				startTime += deltaTime;
//    			INVP(PORT_B0);
        	}
        	while ((endIdx+1 < PACKED_NOTES_LENGTH) && (endTime <= corTime)) {
        		endIdx++;
        		int packedNote = PACKED_NOTES(endIdx);
				int deltaTime = PACKED_DELTA_TIMES((packedNote & DELTATIME_BITMASK) >> DELTATIME_BITOFFSET);
				endTime += deltaTime;
        	}

//        	playingNotesFreq[0] = 440;
//        	playingNotesFreq[1] = 880;
//        	playingNotesFreq[2] = 660;
//        	currentNotes = 3;

        	int time = startTime;
	        currentNotes = 0;
	        for (int l = startIdx; l <= endIdx; l++) {
        		int packedNote = PACKED_NOTES(l);
				int keyIndex = (packedNote & NOTE_BITMASK) >> NOTE_BITOFFSET;
				int keyFreq = PACKED_FREQUENCIES(keyIndex);
				int deltaTimeIndex = (packedNote & DELTATIME_BITMASK) >> DELTATIME_BITOFFSET;
				int deltaTime = PACKED_DELTA_TIMES(deltaTimeIndex);
				if (l>startIdx)
					time += deltaTime;
				int durationIndex = (packedNote & DURATION_BITMASK) >> DURATION_BITOFFSET;
				int duration = PACKED_DURATIONS(durationIndex);
				if ((corTime>time) && (corTime<=time+duration) && (currentNotes < SIMULTANEOUS_NOTES)) {
					playingNotesFreq[currentNotes] = keyFreq;
//								playingNotes[currentNotes][1] = time;
//								playingNotes[currentNotes][2] = duration;
					currentNotes++;
				}
			}


		}


//		OCR0 = (t>>0) & 0x1F;
	}

}
