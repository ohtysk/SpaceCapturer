package info.doyeah.api;

import android.text.format.Time;

public class Game {
	private boolean started = false;
	private MainActivity main;
    float rotateDelta = 0;
    float scaleX = 0.5f;
    float scaleY = 0.5f;
    float scaleZ = 0.5f;
    float positionX = 0;
    float positionY = 0;
    float positionZ = 0;
    float speedX = 0;
    float speedY = 0;
    float speedZ = 0;
    public int score = 0;
    final int step = 1;
    static final float TIME_DELTA = 0.3f;
    private Time startTime = new Time();
    long counter = 0;
    private Runnable touched = new Runnable() {
		public void run() {
			main.mOverlayView.show3DToast("touched!");        			
		}
	};
	public Game(MainActivity main) {
		this.main = main;
	}
	public boolean started() {
		return started;
	}
	public void start() {
		started = true;
		score = 0;
	    rotateDelta = 0;
	    scaleX = 0.5f;
	    scaleY = 0.5f;
	    scaleZ = 0.5f;
	    positionX = 0;
	    positionY = 0;
	    positionZ = 0;
	    speedX = 0;
	    speedY = 0;
	    speedZ = 0;
		positionZ = -main.mObjectDistance;
		startTime.setToNow();
    	main.mHandler.post(new Runnable() {
    		public void run() {
    	        main.mOverlayView.showLast3DToast("Touch view to start!");
    		}
    	});
	}
	public void end() {
		started = false;
		Time endTime = new Time();
		endTime.setToNow();
		final long millis = endTime.toMillis(true) - startTime.toMillis(true);
    	main.mHandler.post(new Runnable() {
    		public void run() {
    			int seconds = (int) (millis / 1000);
    			main.mOverlayView.showLast3DToast(String.format("Congratulations!%nscore=%d in %d seconds%nTouch view to restart!", score, seconds));    			
    		}
    	});
	}
	public void update() {
		if (!started) {
			return;
		}
		counter++;
        main.cube1.setIdentity();
		if (score < step) {
			stage1();
		} else if (score < 2 * step) {
			stage2();
		} else if (score < 3 * step) {
			stage3();
		} else if (score < 4 * step) {
			stage4();
		} else if (score < 5 * step) {
			stage5();
		}
	}
	private void stage1() {
        main.cube1.translate(positionX, positionY, positionZ);
        // Build the Model part of the ModelView matrix.
        rotateDelta += TIME_DELTA;
        main.cube1.rotate(rotateDelta, 0.5f, 0.5f, 1.0f);

        main.cube1.scale(scaleX, scaleY, scaleZ);
        boolean include = main.cube1.include(main.location);
        if (include) {
        	score++;
        	positionX = (float) (Math.random() * main.mObjectDistance - main.mObjectDistance / 2);
        	positionY = (float) (Math.random() * main.mObjectDistance / 2 - main.mObjectDistance / 2);
        	positionZ = (float) (Math.random() * main.mObjectDistance + main.mObjectDistance / 2);
        	main.mHandler.post(touched);
        	main.mVibrator.vibrate(50);
        }
	}
	private void stage2() {
        positionX += speedX;
        positionY += speedY;
        positionZ += speedZ;
        main.cube1.translate(positionX, positionY, positionZ);
        // Build the Model part of the ModelView matrix.
        rotateDelta += TIME_DELTA;
        main.cube1.rotate(rotateDelta, 0.5f, 0.5f, 1.0f);

        main.cube1.scale(scaleX, scaleY, scaleZ);
        
        boolean include = main.cube1.include(main.location);
        if (include) {
        	score++;
        	positionX = (float) (Math.random() * main.mObjectDistance - main.mObjectDistance / 2);
        	positionY = (float) (Math.random() * main.mObjectDistance / 2 - main.mObjectDistance / 2);
        	positionZ = (float) (Math.random() * main.mObjectDistance + main.mObjectDistance / 2);
        	speedX = (float) ((Math.random() - 0.5) * main.UNIT_SPEED / 3);
        	speedY = (float) ((Math.random() - 0.5) * main.UNIT_SPEED / 3);
        	speedZ = (float) ((Math.random() - 0.5) * main.UNIT_SPEED / 3);
        	main.mHandler.post(touched);
        	main.mVibrator.vibrate(50);
        }
	}
	public void stage3() {
		int cycle = (int) (counter % 180);
		double radian = cycle * Math.PI / 90;
		float factor = (float) (Math.cos(radian));
		float speedFactor = (factor + 7) / 8;
		positionX += speedX * speedFactor;
        positionY += speedY * speedFactor;
        positionZ += speedZ * speedFactor;
        main.cube1.translate(positionX, positionY, positionZ);
        // Build the Model part of the ModelView matrix.
        rotateDelta += TIME_DELTA;
        main.cube1.rotate(rotateDelta, 0.5f, 0.5f, 1.0f);

        float scaleFactor = (-factor + 7) / 8;
        main.cube1.scale(scaleX * scaleFactor, scaleY * scaleFactor, scaleZ * scaleFactor);
        
        boolean include = main.cube1.include(main.location);
        if (include) {
        	score++;
        	positionX = (float) (Math.random() * main.mObjectDistance - main.mObjectDistance / 2);
        	positionY = (float) (Math.random() * main.mObjectDistance / 2 - main.mObjectDistance / 2);
        	positionZ = (float) (Math.random() * main.mObjectDistance + main.mObjectDistance / 2);
        	speedX = (float) ((Math.random() - 0.5) * main.UNIT_SPEED / 3);
        	speedY = (float) ((Math.random() - 0.5) * main.UNIT_SPEED / 3);
        	speedZ = (float) ((Math.random() - 0.5) * main.UNIT_SPEED / 3);
        	main.mHandler.post(touched);
        	main.mVibrator.vibrate(50);
        }
	}
	public void stage4() {
		int cycle = (int) (counter % 180);
		if (cycle == 0) {
        	speedX = (float) ((Math.random() - 0.5) * main.UNIT_SPEED / 3);
        	speedY = (float) ((Math.random() - 0.5) * main.UNIT_SPEED / 3);
        	speedZ = (float) ((Math.random() - 0.5) * main.UNIT_SPEED / 3);
		}
		double radian = cycle * Math.PI / 90;
		float factor = (float) (Math.cos(radian));
		float speedFactor = (factor + 7) / 4;
		positionX += speedX * speedFactor;
        positionY += speedY * speedFactor;
        positionZ += speedZ * speedFactor;
        main.cube1.translate(positionX, positionY, positionZ);
        // Build the Model part of the ModelView matrix.
        rotateDelta += TIME_DELTA;
        main.cube1.rotate(rotateDelta, 0.5f, 0.5f, 1.0f);

        float scaleFactor = (-factor + 7) / 8;
        main.cube1.scale(scaleX * scaleFactor, scaleY * scaleFactor, scaleZ * scaleFactor);
        
        boolean include = main.cube1.include(main.location);
        if (include) {
        	score++;
        	positionX = (float) (Math.random() * main.mObjectDistance - main.mObjectDistance / 2);
        	positionY = (float) (Math.random() * main.mObjectDistance / 2 - main.mObjectDistance / 2);
        	positionZ = (float) (Math.random() * main.mObjectDistance + main.mObjectDistance / 2);
        	speedX = (float) (Math.random() * main.UNIT_SPEED / 3);
        	speedY = (float) (Math.random() * main.UNIT_SPEED / 3);
        	speedZ = (float) (Math.random() * main.UNIT_SPEED / 3);
        	main.mHandler.post(touched);
        	main.mVibrator.vibrate(50);
        }
	}
	public void stage5() {
		int cycle = (int) (counter % 5);
		if (cycle == 0) {
	       	speedX = (float) ((Math.random() - 0.5) * main.UNIT_SPEED * 4);
	       	speedY = (float) ((Math.random() - 0.5) * main.UNIT_SPEED * 4);
	       	speedZ = (float) ((Math.random() - 0.5) * main.UNIT_SPEED * 4);
		}
		positionX += speedX;
        positionY += speedY;
        positionZ += speedZ;
        main.cube1.translate(positionX, positionY, positionZ);
        // Build the Model part of the ModelView matrix.
        rotateDelta += TIME_DELTA;
        main.cube1.rotate(rotateDelta, 0.5f, 0.5f, 1.0f);

        main.cube1.scale(scaleX, scaleY, scaleZ);
        
        boolean include = main.cube1.include(main.location);
        if (include) {
        	score++;
        	positionX = (float) (Math.random() * main.mObjectDistance - main.mObjectDistance / 2);
        	positionY = (float) (Math.random() * main.mObjectDistance / 2 - main.mObjectDistance / 2);
        	positionZ = (float) (Math.random() * main.mObjectDistance + main.mObjectDistance / 2);
        	main.mVibrator.vibrate(50);
        	if (score == 5 * step) {
        		end();
        	} else {
            	main.mHandler.post(touched);        		
        	}
        }		
	}
}
