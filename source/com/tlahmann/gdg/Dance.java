package com.tlahmann.gdg;

import com.tlahmann.gdg.controllers.AniImporter;
import com.tlahmann.gdg.controllers.CustomAnimation;
import controlP5.ControlP5;
import ddf.minim.AudioPlayer;
import ddf.minim.Minim;
import ddf.minim.analysis.FFT;
import de.looksgood.ani.Ani;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PShape;

import java.util.ArrayList;
import java.util.Collections;

public class Dance extends PApplet {
    private GUI gui;
    private Player audioPlayer;

    private ComplexObject complexObject;

    @Override
    public void settings() {
        setSize(720, 540);
    }

    @Override
    public void setup() {
        gui = new GUI(this, new Player(this, "./resources/621139_TheFatRat---Time-Lapse.mp3"));

        complexObject = new ComplexObject(this);
    }

    @Override
    public void draw() {
        background(255);
        strokeWeight(1);
        stroke(0);
        noFill();

        complexObject.update(gui.audioPlayer.getSong().position());
        complexObject.draw();
    }

    public void keyPressed() {
        if (key == 'G' || key == 'g') {
            if (gui.cp5.isVisible()) {
                gui.cp5.hide();
            } else {
                gui.cp5.show();
            }
        }
    }

    /**
     * Main method to instantiate the PApplet.
     *
     * @param args Arguments passed to the PApplet.
     */
    public static void main(String[] args) {
        PApplet.main(new String[]{Dance.class.getName()});
    }

    private class GUI {
        private Dance m_canvas;
        private Player audioPlayer;

        ControlP5 cp5;
        int c = 0;

        private GUI(Dance canvas, Player audioPlayer) {
            m_canvas = canvas;
            this.audioPlayer = audioPlayer;

            cp5 = new ControlP5(canvas);

            cp5.addButton("Play/Pause")
                    .setPosition(canvas.width / 2 - 100, canvas.height / 2 + 100)
                    .setSize(200, 19)
                    .plugTo(this, "playPause");

            cp5.addButton("Black/White")
                    .setPosition(canvas.width / 2 - 100, canvas.height / 2 + 130)
                    .setSize(95, 19)
                    .plugTo(this, "blackWhite");

            cp5.addButton("Random Color")
                    .setPosition(canvas.width / 2 + 5, canvas.height / 2 + 130)
                    .setSize(95, 19)
                    .plugTo(this, "randomColor");
        }

        public void playPause(int value) {
            audioPlayer.toggleReplay();
            cp5.hide();
        }

        public void blackWhite(int value) {
            m_canvas.complexObject.changeColor(0);
        }

        public void randomColor(int value) {
            m_canvas.complexObject.changeColor(-1);
        }
    }

    public class Player {
        private AudioPlayer _song;
        private FFT _fft;

        Player(PApplet parent, String file) {
            Minim minim = new Minim(parent);
            _song = minim.loadFile(file);
            _fft = new FFT(_song.bufferSize(), _song.sampleRate());
        }

        public void startPlaying() {
            if (!_song.isPlaying()) {
                _song.play();
            }
        }

        public void pausePlaying() {
            if (_song.isPlaying()) {
                _song.pause();
            }
        }

        public void stopPlaying() {
            if (_song.isPlaying()) {
                _song.pause();
                _song.cue(0);
            }
        }

        void toggleReplay() {
            if (!_song.isPlaying()) {
                _song.play();
            } else {
                _song.pause();
                _song.cue(0);
            }
        }

        public FFT getFFT() {
            return this._fft;
        }

        public AudioPlayer getSong() {
            return this._song;
        }
    }

    public class ComplexObject {
        private PApplet m_canvas;
        private float m_positionX;
        private float m_positionY;
        private float m_scale;
        private float m_rotation;

        private PShape m_shape;

        private ArrayList<CustomAnimation> anis;
        CustomAnimation ani;

        ComplexObject(PApplet papa) {
            m_canvas = papa;
            m_positionX = papa.width / 2;
            m_positionY = papa.height / 2;
            m_scale = 0.1f;
            m_rotation = 0.0f;

            // Draw 10 circles
            m_shape = m_canvas.createShape(PConstants.GROUP);
            float radius = 50;
            float radChange = 70;
            for (int i = 0; i < 10; i++) {
                PShape s = m_canvas.createShape(PConstants.ELLIPSE, 0, 0, radius, radius);
                s.setStroke(0);
                s.setFill(false);
                m_shape.addChild(s);
                radius += radChange;
                radChange *= 0.75f;
            }
            m_canvas.rectMode(PConstants.CENTER);
            // Draw 4 squares
            for (int i = 0; i < 4; i++) {
                float size = 150 + i * 25;
                PShape s = m_canvas.createShape(PConstants.RECT, 0, 0, size, size);
                s.setStroke(0);
                s.setFill(false);
                m_shape.addChild(s);
            }

            Ani.init(m_canvas);
            // import Animations
            anis = AniImporter.importAnimation(m_canvas, "./resources/complexObject.json", "m_scale");
            anis.addAll(AniImporter.importAnimation(m_canvas, "./resources/complexObject.json", "m_rotation"));
            anis.addAll(AniImporter.importAnimation(m_canvas, "./resources/complexObject.json", "m_positionX"));
            anis.addAll(AniImporter.importAnimation(m_canvas, "./resources/complexObject.json", "m_positionY"));
            Collections.sort(anis);
        }

        void draw() {
            if (ani != null) {
                Ani.to(this, ani.duration, ani.params, ani.value, ani.mode);
                ani = null;
            }
            m_canvas.pushMatrix();
            m_canvas.translate(m_positionX, m_positionY);
            m_canvas.scale(m_scale);
            m_canvas.rotate(m_rotation);
            m_canvas.shape(m_shape);
            m_canvas.popMatrix();
        }

        void update(float cue) {
            if (anis.size() == 0) {
                return;
            }
            if (cue / 1000 < anis.get(0).start) {
                return;
            }

            ani = anis.remove(0);
        }

        void changeColor(int color) {
            for (int i = m_shape.getChildCount() - 1; i >= 0; i--) {
                int c = color == -1 ? m_canvas.color(m_canvas.random(255), m_canvas.random(255), m_canvas.random(255)) : color;
                m_shape.getChild(i).setStroke(c);
            }
        }
    }
}
