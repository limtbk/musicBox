import javax.sound.sampled.*;

public class MusicPlay {

	public static final float BUFFER_SIZE_SECONDS = (float) 1;
	public static final int CHANNELS = 1;
	public static final int BITS_PER_SAMPLE = 8;
	public static final int SAMPLE_RATE_POW = 15; //2^15 = 32768
	public static final int SAMPLE_RATE = (1<<SAMPLE_RATE_POW); //32768
	public static final int SAMPLE_RATE_MASK = (SAMPLE_RATE - 1);
	public static final int BUFFER_SIZE_SAMPLES = (int) (SAMPLE_RATE * BUFFER_SIZE_SECONDS);
//	public static final float NOTE_A0 = (float) 13.75;
	
	public static final int NOTE_QUATER = 46;
	public static final int TEMPO_QUATERS_PER_MINUTE = 400;
	public static final int TEMPO_SAMPLES = SAMPLE_RATE/((TEMPO_QUATERS_PER_MINUTE/60)*NOTE_QUATER);

	public static final int SIMULTANEOUS_NOTES = 8;

	static int PACKED_NOTES_BITS = 6;
	static int PACKED_DELTATIMES_BITS = 3;
	static int PACKED_DURATIONS_BITS = 3;
	static final int NOTE_BITOFFSET = 0;
	static final int NOTE_BITMASK = (((1<<PACKED_NOTES_BITS) - 1)<<NOTE_BITOFFSET);
	
	static final int DELTATIME_BITOFFSET = NOTE_BITOFFSET + PACKED_NOTES_BITS;
	static final int DELTATIME_BITMASK = (((1<<PACKED_DELTATIMES_BITS) - 1)<<DELTATIME_BITOFFSET);
	
	static final int DURATION_BITOFFSET = DELTATIME_BITOFFSET + PACKED_DELTATIMES_BITS;
	static final int DURATION_BITMASK = (((1<<PACKED_DURATIONS_BITS) - 1)<<DURATION_BITOFFSET);
	
	static int[] packedDeltaTimes = {0, 16, 32, 48, 64, 96, 144, 192};
	static int[] packedDurations = {46, 48, 62, 94, 160, 528, 720};
	static int[] packedFrequencies = {165, 175, 220, 233, 247, 262, 277, 294, 311, 330, 349, 370, 392, 415, 440, 466, 494, 554, 587, 622, 659, 698, 740, 784, 831, 880, 988, 1047, 1109, 1175, 1245, 1319, 1480, 1760};
	static int[] packedNotes = {19, 28, 4, 211, 28, 4, 339, 28, 4, 339, 25, 4, 211, 28, 4, 856, 543, 532, 2516, 521, 465, 25, 9, 398, 20, 6, 393, 17, 2, 398, 22, 7, 336, 24, 9, 335, 23, 8, 206, 22, 7, 1358, 1044, 1030, 1300, 1052, 1038, 1304, 1055, 1041, 281, 32, 18, 342, 29, 16, 1752, 1567, 1553, 342, 28, 14, 337, 25, 11, 210, 26, 13, 208, 24, 9, 401, 25, 9, 398, 20, 6, 393, 17, 2, 398, 22, 7, 336, 24, 9, 335, 23, 8, 206, 22, 7, 1358, 1044, 1030, 1300, 1052, 1038, 1304, 1055, 1041, 281, 32, 18, 342, 29, 16, 1752, 1567, 1553, 342, 28, 14, 337, 25, 11, 210, 26, 13, 208, 24, 9, 386, 348, 31, 219, 30, 9, 218, 29, 216, 27, 206, 217, 28, 199, 209, 21, 210, 22, 212, 25, 14, 334, 22, 14, 209, 25, 7, 210, 26, 194, 348, 31, 219, 30, 6, 218, 29, 216, 27, 201, 217, 28, 14, 349, 31, 33, 349, 31, 33, 221, 31, 33, 329, 322, 348, 31, 219, 30, 9, 218, 29, 216, 27, 206, 217, 28, 199, 209, 21, 210, 22, 212, 25, 14, 334, 22, 14, 209, 25, 7, 210, 26, 194, 341, 27, 10, 402, 26, 12, 401, 25, 14, 393, 201, 322, 322, 348, 31, 219, 30, 9, 218, 29, 216, 27, 206, 217, 28, 199, 209, 21, 210, 22, 212, 25, 14, 334, 22, 14, 209, 25, 7, 210, 26, 194, 348, 31, 219, 30, 6, 218, 29, 216, 27, 201, 217, 28, 14, 349, 31, 33, 349, 31, 33, 221, 31, 33, 329, 322, 348, 31, 219, 30, 9, 218, 29, 216, 27, 206, 217, 28, 199, 209, 21, 210, 22, 212, 25, 14, 334, 22, 14, 209, 25, 7, 210, 26, 194, 341, 27, 10, 402, 26, 12, 401, 25, 14, 393, 201, 322, 341, 25, 1, 213, 25, 341, 25, 5, 341, 25, 215, 26, 10, 340, 28, 9, 209, 25, 337, 22, 2, 206, 20, 320, 341, 25, 1, 213, 25, 341, 25, 5, 341, 25, 215, 26, 10, 1748, 1564, 201, 386, 384, 341, 25, 1, 213, 25, 341, 25, 5, 341, 25, 215, 26, 10, 340, 28, 9, 209, 25, 337, 22, 2, 206, 20, 320, 339, 28, 4, 211, 28, 4, 339, 28, 4, 339, 25, 4, 211, 28, 4, 856, 543, 532, 2516, 521, 465, 25, 9, 398, 20, 6, 393, 17, 2, 398, 22, 7, 336, 24, 9, 335, 23, 8, 206, 22, 7, 1358, 1044, 1030, 1300, 1052, 1038, 1304, 1055, 1041, 281, 32, 18, 342, 29, 16, 1752, 1567, 1553, 342, 28, 14, 337, 25, 11, 210, 26, 13, 208, 24, 9, 401, 25, 9, 398, 20, 6, 393, 17, 2, 398, 22, 7, 336, 24, 9, 335, 23, 8, 206, 22, 7, 1358, 1044, 1030, 1300, 1052, 1038, 1304, 1055, 1041, 281, 32, 18, 342, 29, 16, 1752, 1567, 1553, 342, 28, 14, 337, 25, 11, 210, 26, 13, 208, 24, 9, 409, 28, 2, 214, 25, 337, 20, 8, 201, 337, 21, 14, 338, 22, 7, 217, 29, 199, 217, 29, 210, 22, 14, 206, 199, 1364, 1048, 4, 1309, 1056, 1309, 1056, 71, 1245, 1056, 9, 1308, 1055, 141, 1178, 1053, 281, 28, 9, 214, 25, 201, 210, 22, 209, 20, 14, 206, 201, 345, 28, 2, 214, 25, 337, 20, 8, 201, 337, 21, 14, 338, 22, 7, 217, 29, 199, 217, 29, 210, 22, 14, 206, 199, 340, 24, 9, 218, 29, 346, 29, 9, 1242, 1053, 1033, 1305, 1052, 1035, 1304, 1050, 1037, 276, 25, 14, 209, 201, 209, 206, 2, 473, 28, 2, 214, 25, 337, 20, 8, 201, 337, 21, 14, 338, 22, 7, 217, 29, 199, 217, 29, 210, 22, 14, 206, 199, 1364, 1048, 4, 1309, 1056, 1309, 1056, 71, 1245, 1056, 9, 1308, 1055, 141, 1178, 1053, 281, 28, 9, 214, 25, 201, 210, 22, 209, 20, 14, 206, 201, 345, 28, 2, 214, 25, 337, 20, 8, 201, 337, 21, 14, 338, 22, 7, 217, 29, 199, 217, 29, 210, 22, 14, 206, 199, 340, 24, 9, 218, 29, 346, 29, 9, 1242, 1053, 1033, 1305, 1052, 1035, 1304, 1050, 1037, 276, 25, 14, 209, 201, 209, 206, 2, 467, 28, 4, 211, 28, 4, 339, 28, 4, 339, 25, 4, 211, 28, 4, 856, 543, 532, 2516, 521, 473, 28, 2, 214, 25, 337, 20, 8, 201, 337, 21, 14, 338, 22, 7, 217, 29, 199, 217, 29, 210, 22, 14, 206, 199, 1364, 1048, 4, 1309, 1056, 1309, 1056, 71, 1245, 1056, 9, 1308, 1055, 141, 1178, 1053, 281, 28, 9, 214, 25, 201, 210, 22, 209, 20, 14, 206, 201, 345, 28, 2, 214, 25, 337, 20, 8, 201, 337, 21, 14, 338, 22, 7, 217, 29, 199, 217, 29, 210, 22, 14, 206, 199, 340, 24, 9, 218, 29, 346, 29, 9, 1242, 1053, 1033, 1305, 1052, 1035, 1304, 1050, 1037, 276, 25, 14, 209, 201, 209, 206, 2, 465, 25, 9, 398, 20, 6, 397, 17, 2, 1366, 2066, 2055, 1304, 1302, 1301, 2066, 2051, 1303, 1301, 273, 3092, 3074, 208, 2769};
	
	public static void main(String[] args) {
		byte[] buf = new byte[BUFFER_SIZE_SAMPLES];
		int[][] playingNotes = new int[SIMULTANEOUS_NOTES][2]; //freq, time, restDuration, index
	    AudioFormat af = new AudioFormat((float)SAMPLE_RATE, BITS_PER_SAMPLE, CHANNELS, true, false);

		try {
			SourceDataLine sdl = AudioSystem.getSourceDataLine(af);
		    sdl.open(af);
		    sdl.start();

		    int startIdx = 0;
		    int startTime = 0;
		    int startDuration = packedDurations[(packedNotes[startIdx] & DURATION_BITMASK) >> DURATION_BITOFFSET];

		    int endIdx = 0;
		    int endTime = 0;
		    
		    int lastTime = -1;
		    int currentNotes = 0;
		    
		    long t = 0;
		    for (int k = 0; k < 1; ) {
		    	for(int i = 0; i < BUFFER_SIZE_SAMPLES; i++, t++) {
			       
			        buf[i] = 0;
			        
			        int corTime = (int) (t/TEMPO_SAMPLES);
			        if (corTime > lastTime) {
			        	lastTime = corTime;
			        	
			        	while ((startIdx+1 < packedNotes.length) && (startTime + startDuration <= corTime)) {
			        		startIdx++;
							int deltaTime = packedDeltaTimes[(packedNotes[startIdx] & DELTATIME_BITMASK) >> DELTATIME_BITOFFSET];
						    startDuration = packedDurations[(packedNotes[startIdx] & DURATION_BITMASK) >> DURATION_BITOFFSET];
							startTime += deltaTime;
			        	}

			        	while ((endIdx+1 < packedNotes.length) && (endTime <= corTime)) {
			        		endIdx++;
							int deltaTime = packedDeltaTimes[(packedNotes[endIdx] & DELTATIME_BITMASK) >> DELTATIME_BITOFFSET];
							endTime += deltaTime;
			        	}

			        	int time = startTime;
				        currentNotes = 0;
				        for (int l = startIdx; l <= endIdx; l++) {
							int keyIndex = (packedNotes[l] & NOTE_BITMASK) >> NOTE_BITOFFSET;
							int keyFreq = packedFrequencies[keyIndex];
							int deltaTimeIndex = (packedNotes[l] & DELTATIME_BITMASK) >> DELTATIME_BITOFFSET;
							int deltaTime = packedDeltaTimes[deltaTimeIndex];
							if (l>startIdx)
								time += deltaTime;
							int durationIndex = (packedNotes[l] & DURATION_BITMASK) >> DURATION_BITOFFSET;
							int duration = packedDurations[durationIndex];
							if ((corTime>time) && (corTime<=time+duration) && (currentNotes < SIMULTANEOUS_NOTES)) {
								playingNotes[currentNotes][0] = keyFreq;
//								playingNotes[currentNotes][1] = time;
//								playingNotes[currentNotes][2] = duration;
								currentNotes++;
							}
						}
			        	
			        }
			        
			        for (int l = 0; l < currentNotes; l++) {
						int keyFreq = playingNotes[l][0];
						int phase = playingNotes[l][1];
						phase += keyFreq;
						playingNotes[l][1] = phase;
						int ab = (phase) & SAMPLE_RATE_MASK;
				        buf[i] += (byte) ((ab<(SAMPLE_RATE/2)?0:20));
					}
			    }
		    	sdl.write( buf, 0, BUFFER_SIZE_SAMPLES );
		    }
		    sdl.drain();
		    sdl.stop();
		    sdl.close();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}

	}

}
