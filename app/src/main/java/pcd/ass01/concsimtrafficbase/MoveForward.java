package pcd.ass01.concsimtrafficbase;

import pcd.ass01.simengineconc.Action;

/**
 * Car agent move forward action
 */
public record MoveForward(double distance) implements Action {}
