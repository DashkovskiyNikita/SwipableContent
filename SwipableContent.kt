@Composable
fun SwipeToReveal(
    modifier: Modifier = Modifier,
    offsetValue: Dp = 100.dp,
    swipeableContent: @Composable BoxScope.() -> Unit,
    rightActionsRow: @Composable BoxScope.() -> Unit
) {
    val density = LocalDensity.current

    val maxOffsetX = remember {
        with(density) { offsetValue.toPx() }
    }

    var currentOffset by remember {
        mutableStateOf(0f)
    }

    val animatedOffset by animateIntOffsetAsState(
        targetValue = IntOffset(x = currentOffset.roundToInt(), y = 0)
    )

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Red),
            contentAlignment = Alignment.CenterEnd,
            content = swipeableContent
        )
        Box(
            modifier = Modifier
                .offset { animatedOffset }
                .pointerInput(Unit) {
                    detectHorizontalDragGestures(
                        onHorizontalDrag = { _, dragAmount ->
                            val newOffset = currentOffset + dragAmount
                            currentOffset = newOffset.coerceIn(-maxOffsetX, 0f)
                        },
                        onDragEnd = {
                            currentOffset =
                                if (maxOffsetX / 2 <= -currentOffset) -maxOffsetX else 0f
                        }
                    )
                },
            content = rightActionsRow
        )
    }
}
