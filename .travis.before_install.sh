#!/usr/bin/env bash

# Install licenses if available

if [ "$TESTBENCH_LICENSE" != "" ] && [ ! -e ~/.vaadin.testbench.developer.license ]
then
	echo "Installing TestBench license"
	echo $TESTBENCH_LICENSE > ~/.vaadin.testbench.developer.license
fi
if [ "$CHARTS_LICENSE" != "" ] && [ ! -e ~/.vaadin.charts.developer.license ]
then
	echo "Installing Charts license"
	echo $CHARTS_LICENSE > ~/.vaadin.charts.developer.license
fi
if [ "$BOARD_LICENSE" != "" ] && [ ! -e ~/.vaadin.board.developer.license ]
then
	echo "Installing Board license"
	echo $BOARD_LICENSE > ~/.vaadin.board.developer.license
fi
