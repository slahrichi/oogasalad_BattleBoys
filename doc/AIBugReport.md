## Description

Summary of the feature's bug (without describing implementation details)

- In a game with exclusively AI, the game infinitely progresses despite the logs acknowledging that
the game has ended

## Expected Behavior

Describe the behavior you expect

- When an AI wins in a game with only AI, the game loop should stop

## Current Behavior

Describe the actual behavior

- When an AI wins in a game with only AI, the game infinitely loops 

## Steps to Reproduce

Provide detailed steps for reproducing the issue.

1. Create a game with only AI players
2. The game will begin and infinitely run and produce logs

## Failure Logs

Include any relevant print/LOG statements, error messages, or stack traces.

- The logs printed forever while acknowledging that one player had already won

## Hypothesis for Fixing the Bug

This is not a test that can be caught as well with unit tests because it's an infinite loop. A test
with a timeout could technically catch this, but simply running the game with only AI reveals the
bug. The issue is that AI do not know when the game is over unlike humans, so doing so during the 
turn passing logic of the code should instantly resolve this issue. Note: this did indeed solve the
issue.