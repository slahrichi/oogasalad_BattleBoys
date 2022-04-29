## Description

When parsing an island, the returned island is null.

## Expected Behavior

We expect the island to not be null. 

## Current Behavior

The island is null. 

## Steps to Reproduce

Using the game creator, create a game with special islands. Load this game and the special islands will not be there. 

## Failure Logs

These come from debugging the program and identifying the null Special Islands. 

## Hypothesis for Fixing the Bug

I think the Hierarchy Adapters are incorrectly setup within ParseSpecialIslands. 