import { useState } from 'react';
import { Link } from 'react-router-dom';
import { Heart, Trash2 } from 'lucide-react';
import { Button } from '@/components/ui/button';
import { Card, CardContent, CardFooter, CardHeader } from '@/components/ui/card';
import { Avatar, AvatarFallback } from '@/components/ui/avatar';
import { likesAPI, postsAPI } from '@/lib/api';
import { toast } from 'sonner';
import { useAuth } from '@/contexts/AuthContext';

interface Post {
  id: string;
  authorUsername: string;
  content: string;
  createdAt: string;
  liked: boolean;
  likedCount: number;
}

interface PostCardProps {
  post: Post;
  onDelete?: (postId: string) => void;
  onLike?: (post: any) => void;
  onUnlike?: (postId: string) => void;
  onLikeToggle?: (postId: string, liked: boolean) => void;
}

export function PostCard({ post, onLike, onDelete, onUnlike, onLikeToggle }: PostCardProps) {
  const { userInfo } = useAuth();
  const [liked, setLiked] = useState(post.liked);
  const [likeCount, setLikeCount] = useState(post.likedCount);
  const [loading, setLoading] = useState(false);

  const handleLikeToggle = async () => {
    if (loading) return;
    setLoading(true);

    const newLikedState = !liked;
    setLiked(newLikedState);
    setLikeCount((prev) => prev + (newLikedState ? 1 : -1));

    try {
      if (newLikedState) {
        await likesAPI.likePost(post.id);
        const likedPost = { ...post, likedCount: likeCount + 1, liked: true };
        onLike?.(likedPost);
      }
      else {
        await likesAPI.unlikePost(post.id);
        onUnlike?.(post.id);
      }
      onLikeToggle?.(post.id, newLikedState);
    } catch (error) {
      setLiked(!newLikedState);
      setLikeCount((prev) => prev + (newLikedState ? -1 : 1));
      toast.error('Failed to update like');
    } finally {
      setLoading(false);
    }
  };

  const handleDelete = async () => {
    if (!confirm('Are you sure you want to delete this post?')) return;

    try {
      await postsAPI.deletePost(post.id);
      toast.success('Post deleted');
      onDelete?.(post.id);
    } catch (error) {
      toast.error('Failed to delete post');
    }
  };

  const isOwner = userInfo?.username === post.authorUsername;

  return (
    <Card className="transition-shadow hover:shadow-md">
      <CardHeader className="flex flex-row items-center space-x-4 pb-3">
        <Link to={`/user/${post.authorUsername}`}>
          <Avatar>
            <AvatarFallback className="bg-primary text-primary-foreground">
              {post.authorUsername[0].toUpperCase()}
            </AvatarFallback>
          </Avatar>
        </Link>
        <div className="flex-1">
          <Link to={`/user/${post.authorUsername}`} className="hover:underline">
            <p className="font-semibold">@{post.authorUsername}</p>
          </Link>
          <p className="text-xs text-muted-foreground">
            {new Date(post.createdAt).toLocaleDateString()} at{' '}
            {new Date(post.createdAt).toLocaleTimeString()} {' '}
            {post.id}
          </p>
        </div>
        {isOwner && (
          <Button
            size="icon"
            variant="ghost"
            onClick={handleDelete}
            className="text-destructive hover:bg-destructive/10"
          >
            <Trash2 className="h-4 w-4" />
          </Button>
        )}
      </CardHeader>
      <CardContent>
        <p className="whitespace-pre-wrap">{post.content}</p>
      </CardContent>
      <CardFooter>
        <Button
          variant="ghost"
          size="sm"
          onClick={handleLikeToggle}
          disabled={loading}
          className={liked ? 'text-destructive hover:text-destructive/90' : 'hover:text-primary'}
        >
          <Heart className={`mr-2 h-4 w-4 ${liked ? 'fill-current' : ''}`} />
          {likeCount}
        </Button>
      </CardFooter>
    </Card>
  );
}
