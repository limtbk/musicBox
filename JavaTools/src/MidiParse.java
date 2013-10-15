import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;

public class MidiParse {
    public static final int NOTE_ON = 0x90;
    public static final int NOTE_OFF = 0x80;
    public static final String[] NOTE_NAMES = {"C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B"};
	static int[] frequencies = {14, 15, 15, 16, 17, 18, 19, 21, 22, 23, 24, 26, 28, 29, 31, 33, 35, 37, 39, 41, 44, 46, 49, 52, 55, 58, 62, 65, 69, 73, 78, 82, 87, 92, 98, 104, 110, 117, 123, 131, 139, 147, 156, 165, 175, 185, 196, 208, 220, 233, 247, 262, 277, 294, 311, 330, 349, 370, 392, 415, 440, 466, 494, 523, 554, 587, 622, 659, 698, 740, 784, 831, 880, 932, 988, 1047, 1109, 1175, 1245, 1319, 1397, 1480, 1568, 1661, 1760, 1865, 1976, 2093, 2217, 2349, 2489, 2637, 2794, 2960, 3136, 3322, 3520, 3729, 3951, 4186, 4435, 4699, 4978, 5274, 5588, 5920, 6272, 6645, 7040, 7459, 7902, 8372, 8870, 9397, 9956, 10548, 11175, 11840, 12544, 13290, 14080, 14917, 15804, 16744, 17740, 18795, 19912, 21096, 22351, 23680, 25088, 26580, 28160, 29834, 31609, 33488, 35479, 37589, 39824, 42192, 44701, 47359, 50175, 53159, 56320, 59669, 63217, 66976, 70959, 75178, 79649, 84385, 89402, 94719, 100351, 106318, 112640, 119338, 126434, 133952, 141918, 150356, 159297, 168769, 178805, 189437, 200702, 212636, 225280, 238676, 252868, 267905, 283835, 300713, 318594, 337539, 357610, 378874, 401403, 425272, 450560, 477352, 505737, 535809, 567670, 601425, 637188, 675077, 715219, 757749, 802807, 850544, 901120, 954703, 1011473, 1071618, 1135340, 1202851, 1274376, 1350155, 1430439, 1515497, 1605613, 1701088, 1802240, 1909407, 2022946, 2143237, 2270680, 2405702, 2548752, 2700309, 2860878, 3030994, 3211227, 3402176, 3604480, 3818814, 4045892, 4286474, 4541360, 4811404, 5097505, 5400618, 5721756, 6061989, 6422453, 6804352, 7208960, 7637627, 8091784, 8572947, 9082720, 9622807, 10195009, 10801236, 11443511, 12123977, 12844906, 13608704, 14417920, 15275254, 16183568, 17145894, 18165440, 19245614, 20390018, 21602472, 22887022, 24247954, 25689812, 27217408, 28835840, 30550508, 32367136};

    public static void main(String[] args) throws Exception {
//        Sequence sequence = MidiSystem.getSequence(new File("elka.mid"));
        Sequence sequence = MidiSystem.getSequence(new File("mario.mid"));
//        Sequence sequence = MidiSystem.getSequence(new File("imperial.mid"));
        ArrayList<Map<String, Number>> array = new ArrayList<Map<String, Number>>();
        int trackNumber = 0;
        for (Track track :  sequence.getTracks()) {
            trackNumber++;
//            System.out.println("Track " + trackNumber + ": size = " + track.size());
            System.out.println();
            for (int i=0; i < track.size(); i++) { 
                MidiEvent event = track.get(i);
//                System.out.print("@" + event.getTick() + " ");
                MidiMessage message = event.getMessage();
                if (message instanceof ShortMessage) {
                    ShortMessage sm = (ShortMessage) message;
//                    System.out.print("Channel: " + sm.getChannel() + " ");
                    if (sm.getCommand() == NOTE_ON) {
                        int key = sm.getData1();
                        Map<String, Number> map = new HashMap<String, Number>();
                        map.put("time", event.getTick());
                        map.put("key", key);
                        map.put("channel", sm.getChannel());
                        map.put("track", trackNumber);
                        int octave = (key / 12)-1;
                        int note = key % 12;
                        String noteName = NOTE_NAMES[note];
                        int velocity = sm.getData2();
                        map.put("velocity", velocity);
                        int channel = sm.getChannel();
//                        if ((channel == 5)||(channel == 6)||(channel == 13))
//                        if ((trackNumber == 1)||(trackNumber == 4)||(trackNumber == 5)||(trackNumber == 6)||(trackNumber == 7)||(trackNumber == 13))
                        	array.add(map);
//                        System.out.println("Note on, " + noteName + octave + " key=" + key + " velocity: " + velocity);
                    } else if (sm.getCommand() == NOTE_OFF) {
                        int key = sm.getData1();
                        Map<String, Number> map = new HashMap<String, Number>();
                        map.put("time", event.getTick());
                        map.put("key", key);
                        map.put("channel", sm.getChannel());
                        map.put("track", trackNumber);

                        int channel = sm.getChannel();
//                        if ((trackNumber == 1)||(trackNumber == 4)||(trackNumber == 5)||(trackNumber == 6)||(trackNumber == 7)||(trackNumber == 13))
//                        if ((channel == 5)||(channel == 6)||(channel == 13))
                        	array.add(map);
                        
                        int octave = (key / 12)-1;
                        int note = key % 12;
                        String noteName = NOTE_NAMES[note];
                        int velocity = sm.getData2();
//                        System.out.println("Note off, " + noteName + octave + " key=" + key + " velocity: " + velocity);
                    } else {
//                        System.out.println("Command:" + sm.getCommand());
                    }
                } else {
//                    System.out.println("Other message: " + message.getClass());
                }
            }

        }
        
        System.out.println();
        
        Collections.sort(array, new Comparator<Map<String, Number>>(){
        	public int compare(Map<String, Number> event1, Map<String, Number> event2) {
        		Number time1 = event1.get("time");
        		Number time2 = event2.get("time");
        		Number track1 = event1.get("track");
        		Number track2 = event2.get("track");
        		Number channel1 = event1.get("channel");
        		Number channel2 = event2.get("channel");
        		int result = (time1.longValue() > time2.longValue())?1:((time1.longValue() < time2.longValue())?-1:
        					 (channel1.longValue() > channel2.longValue())?1:((channel1.longValue() < channel2.longValue())?-1:
        				     (track1.longValue() > track2.longValue())?1:((track1.longValue() < track2.longValue())?-1:0)));
        		return result;
        	}
        });
        
        ArrayList<Map<String, Number>> onNotes = new ArrayList<Map<String, Number>>();
        ArrayList<Map<String, Number>> finalNotes = new ArrayList<Map<String, Number>>();
        
//        System.out.println(array);
//        Number lastTime = 0;
        for (int i = 0; i < array.size(); i++) {
        	Map<String, Number> eventMap = array.get(i);
        	
//			Number key = eventMap.get("key");
			Number velocity = eventMap.get("velocity");
//			Number time = eventMap.get("time");

			if ((null != velocity) && (0 < velocity.intValue())) {
//				System.out.printf("%6.0f, %d\n",time.floatValue()/16,key);
//				System.out.printf("%d\n",key);
				onNotes.add(eventMap);
			} else {
				int currIndex = -1;
				for (int j = 0; j < onNotes.size(); j++) {
					Map<String, Number> searchEventMap = onNotes.get(j);
					if ((searchEventMap.get("key")).equals(eventMap.get("key")) &&
						(searchEventMap.get("track")).equals(eventMap.get("track")) &&
						(searchEventMap.get("channel")).equals(eventMap.get("channel"))) {
						currIndex = j;
						break;
					}
				}
				if (currIndex >= 0) {
					Map<String, Number> searchEventMap = onNotes.get(currIndex);
					long duration = eventMap.get("time").longValue() - onNotes.get(currIndex).get("time").longValue();
					searchEventMap.put("duration", duration);
					finalNotes.add(searchEventMap);
					onNotes.remove(currIndex);
				} else {
					//error - end event without start event
				}
			}
			
//			System.out.println(onNotes);
		}
        
        Collections.sort(finalNotes, new Comparator<Map<String, Number>>(){
        	public int compare(Map<String, Number> event1, Map<String, Number> event2) {
        		Number time1 = event1.get("time");
        		Number time2 = event2.get("time");
        		Number track1 = event1.get("track");
        		Number track2 = event2.get("track");
        		Number channel1 = event1.get("channel");
        		Number channel2 = event2.get("channel");
        		int result = (time1.longValue() > time2.longValue())?1:((time1.longValue() < time2.longValue())?-1:
					 (channel1.longValue() > channel2.longValue())?1:((channel1.longValue() < channel2.longValue())?-1:
				     (track1.longValue() > track2.longValue())?1:((track1.longValue() < track2.longValue())?-1:0)));
        		return result;
        	}
        });
        

        
        Number lastTime = 0;
        
        Set<Number> notesSet = new HashSet<Number>();
        Set<Number> deltaTimesSet = new HashSet<Number>();
        Set<Number> durationsSet = new HashSet<Number>();

        for (int i = 0; i < finalNotes.size(); i++) {
        	Map<String, Number> eventMap = finalNotes.get(i);
        	
			Number key = eventMap.get("key");
//			Number velocity = eventMap.get("velocity");
			Number time = eventMap.get("time");
			Number duration = eventMap.get("duration");
			notesSet.add(key);
			deltaTimesSet.add(time.longValue() - lastTime.longValue());
			durationsSet.add(duration);
			
//			System.out.println(eventMap);
//			System.out.printf("%6d, %3d, %3d, %3d\n",time.longValue(),(time.longValue() - lastTime.longValue()), key, duration);
			if (i % 64 == 0) {
				System.out.println();
			}
			System.out.printf("{%5d, %3d, %3d}, ",time.longValue(), key.longValue(), duration.longValue());
//			System.out.printf("{%3d, %3d, %3d}, ",time.longValue() - lastTime.longValue(), key.longValue(), duration.longValue());
//			System.out.printf("{0x%02X, 0x%02X, 0x%02X}, ",(time.longValue()-lastTime.longValue())/16, key.longValue(), duration.longValue()/16);
			lastTime = time;

        }
        
        System.out.println();
        System.out.println(notesSet);
        System.out.println(deltaTimesSet);
        System.out.println(durationsSet);
        
        System.out.println(notesSet.size());
        System.out.println(deltaTimesSet.size());
        System.out.println(durationsSet.size());
        
        int notesBits = (int) Math.ceil(Math.log(notesSet.size())/Math.log(2));
        int deltaTimesBits = (int) Math.ceil(Math.log(deltaTimesSet.size())/Math.log(2));
        int durationsBits = (int) Math.ceil(Math.log(durationsSet.size())/Math.log(2));
        
        System.out.printf("packedNotesBits = %d\n", notesBits);
        System.out.printf("packedDeltaTimesBits = %d\n", deltaTimesBits);
        System.out.printf("packedDurationsBits = %d\n", durationsBits);

        Comparator<Number> numberComparator = new Comparator<Number>(){
        	public int compare(Number value1, Number value2) {
        		int result = (value1.longValue() > value2.longValue())?1:((value1.longValue() < value2.longValue())?-1:0);
        		return result;
        	}
        }; 
        ArrayList<Number> notesArray = new ArrayList<Number>();
        for (Number value : notesSet) {
        	notesArray.add(value);
        }
        Collections.sort(notesArray, numberComparator);
        
        ArrayList<Number> deltaTimesArray = new ArrayList<Number>();
        for (Number value : deltaTimesSet) {
        	deltaTimesArray.add(value);
        }
        Collections.sort(deltaTimesArray, numberComparator);
        
        ArrayList<Number> durationsArray = new ArrayList<Number>();
        for (Number value : durationsSet) {
        	durationsArray.add(value);
        }
        Collections.sort(durationsArray, numberComparator);       
        
//        System.out.println(notesArray);
        System.out.print("packedDeltaTimes = ");
        System.out.println(deltaTimesArray);
        System.out.print("packedDurations = ");
        System.out.println(durationsArray);
        
        System.out.print("packedFrequencies = {");
        for (int i = 0; i < notesArray.size(); i++) {
			System.out.printf("%d, ", frequencies[notesArray.get(i).intValue()]);
		}
        System.out.println("}");

        ArrayList<ArrayList<Number>> packedNotes = new ArrayList<ArrayList<Number>>();
        
        lastTime = 0;

        System.out.printf("packedNotesLength = %d\n", finalNotes.size());
        System.out.print("packedNotes = {");
        for (int i = 0; i < finalNotes.size(); i++) {
        	Map<String, Number> eventMap = finalNotes.get(i);
			Number key = eventMap.get("key");
			Number time = eventMap.get("time");
			Number duration = eventMap.get("duration");

			int keyIndex = notesArray.indexOf(key);
			int deltaTimeIndex = deltaTimesArray.indexOf(time.longValue() - lastTime.longValue());
			int durationIndex = durationsArray.indexOf(duration);
//	        System.out.print(keyIndex);

			long noteLong = 0;
			
			noteLong += keyIndex;
			noteLong += (deltaTimeIndex << notesBits);
			noteLong += (durationIndex << (notesBits + deltaTimesBits));
			System.out.printf("%d, ", noteLong);
			
			ArrayList<Number> a = new ArrayList<Number>();
			a.add(keyIndex);
			a.add(deltaTimeIndex);
			a.add(durationIndex);
			packedNotes.add(a);
			
			lastTime = time;
        }
        System.out.println("}");
        
//        System.out.println(packedNotes);

    }

}
